#! /usr/bin/env python3

import argparse
import subprocess
import shutil
import os
import sys
import modules.namb_variables as vars

CMD_NOT_FOUND_CODE = 127

class CommandNotFound(Exception):
    def __init__(self, cmd):
        self.error_message = "The command '{}' is not an executable".format(cmd)

    def __str__(self):
        return "{}: {}".format(self.__class__.__name__, self.error_message)


def run_storm(custom_bin_path=None, namb_conf=vars.NAMB_CONF, storm_conf=vars.STORM_CONF):
    storm_bin = custom_bin_path if custom_bin_path else 'storm'

    if subprocess.run([storm_bin], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL).returncode != CMD_NOT_FOUND_CODE:
        storm_command = [storm_bin, 'jar', vars.STORM_JAR, vars.STORM_CLASS, namb_conf, storm_conf]
        subprocess.run(storm_command)
        return
    else:
        raise CommandNotFound(storm_bin)


def run_heron(custom_bin_path=None, namb_conf=vars.NAMB_CONF, heron_conf=vars.HERON_CONF):
    heron_bin = custom_bin_path if custom_bin_path else 'heron'

    deployment = "local"
    with open(heron_conf) as conf:
        (key, value) = conf.readline().split(":")
        if key == "deployment":
            deployment = value.split("#")[0].strip()

    if subprocess.run([heron_bin], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL).returncode != CMD_NOT_FOUND_CODE:
        heron_command = [heron_bin, "submit", deployment, vars.HERON_JAR, vars.HERON_CLASS, namb_conf]
        print(heron_command)
        subprocess.run(heron_command)
        return
    else:
        raise CommandNotFound(heron_bin)


def run_flink(custom_bin_path=None, namb_conf=vars.NAMB_CONF, flink_conf=vars.FLINK_CONF, detached=False):
    flink_bin = custom_bin_path if custom_bin_path else 'flink'

    if subprocess.run([flink_bin], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL).returncode != CMD_NOT_FOUND_CODE:
        flink_command = [flink_bin, "run",]
        if detached: flink_command.append("-d")
        flink_command.extend([vars.FLINK_JAR, namb_conf, flink_conf])
        subprocess.run(flink_command)
        return
    else:
        raise CommandNotFound(flink_bin)


def run_spark(custom_bin_path=None, namb_conf=vars.NAMB_CONF, spark_conf=vars.SPARK_CONF, master="local[*]"):

    spark_bin = custom_bin_path if custom_bin_path else 'spark-submit'
    spark_bin = "/Users/mirfarzam/Documents/spark/spark-2.4.4-bin-hadoop2.7/bin/spark-submit"

    if subprocess.run([spark_bin], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL).returncode != CMD_NOT_FOUND_CODE:
        spark_command = [spark_bin, "--master", master, "--class", vars.SPARK_CLASS, vars.SPARK_JAR, namb_conf, spark_conf]
        subprocess.run(spark_command)
        return
    else:
        raise CommandNotFound(spark_bin)


def run(cmd, **kwargs):
    if cmd == 'storm':
        run_storm(kwargs["custom_bin_path"], kwargs["custom_namb_conf"], kwargs["custom_platform_conf"])

    elif cmd == 'heron':
        run_heron(kwargs["custom_bin_path"], kwargs["custom_namb_conf"], kwargs["custom_platform_conf"])

        return
    elif cmd == 'flink':
        run_flink(kwargs["custom_bin_path"], kwargs["custom_namb_conf"], kwargs["custom_platform_conf"], kwargs["detached"])

    elif cmd == 'spark':
        run_spark(kwargs["custom_bin_path"], kwargs["custom_namb_conf"], kwargs["custom_platform_conf"])

    else:
        print("Oh my gosh. You shall not be here... Run fool!")

def initialize_configurations():
    src = vars.CONF_PATH_DEFAULTS
    dst = vars.CONF_PATH

    for file in os.listdir(src):
        shutil.copy(src +"/" + file, dst)


def build(test, keep_files):
    mvn_command = "mvn clean install".split()
    if not test:
        mvn_command.append("-Dmaven.test.skip=true")

    subprocess.run(mvn_command)

    if not keep_files:
        initialize_configurations()

def print_version():
    print("\033[1mNAMB v{}\033[0m".format(vars.NAMB_VERSION))
    print("This is Yet Another MicroBenchmark")
    sys.exit(0)


if __name__ == "__main__":
    # main parser
    main_parser = argparse.ArgumentParser(prog="namb.py")
    main_parser.add_argument("-c", "--conf", dest="namb_conf", metavar="<namb_conf>", help="Specify custom NAMB configuration file", default=vars.NAMB_CONF)
    main_parser.add_argument("-v", "--version", dest="show_version", action="store_true", help="Show tool current version")

    # platform subparsers definition
    subparser = main_parser.add_subparsers(dest="command", metavar="Commands")

    build_parser = subparser.add_parser("build", help="Build project")
    build_parser.add_argument("-t", "--test", dest="tests", action="store_true", help="perform maven test when building project")
    build_parser.add_argument("-k", "--keepfiles", dest="keep_files", action="store_true", help="build project without reneitializing config files")

    # storm subparser
    storm_parser = subparser.add_parser('storm', help='Run Apache Storm benchmark')
    storm_parser.add_argument("-p","--path", dest="exec_path", metavar="<storm_executable>", help="path to Storm executable", default="storm")
    storm_parser.add_argument("-c", "--conf", dest="platform_conf", metavar="<storm_conf>", help="specify custom Storm benchmark configuration file", default=vars.STORM_CONF)

    # heron subparser
    heron_parser = subparser.add_parser('heron', help='Run Apache Heron benchmark')
    heron_parser.add_argument("-p","--path", dest="exec_path", metavar="<heron_executable>", help="path to Heron executable", default="heron")
    heron_parser.add_argument("-c", "--conf", dest="platform_conf", metavar="<heron_conf>", help="specify custom Heron benchmark configuration file", default=vars.HERON_CONF)

    # flink subparser
    flink_parser = subparser.add_parser('flink', help="Run Apache Flink benchmark")
    flink_parser.add_argument("-p","--path", dest="exec_path", metavar="<flink_executable>", help="path to Flink executable", default="flink")
    flink_parser.add_argument("-c", "--conf", dest="platform_conf", metavar="<flink_conf>", help="specify custom Flink benchmark configuration file", default=vars.FLINK_CONF)
    flink_parser.add_argument("-d", "--detached", dest="is_detached", action="store_true", help="run the benchmark in detached mode")

    # spark subparser
    spark_parser = subparser.add_parser('spark', help="Run Apache Spark benchmark")
    spark_parser.add_argument("-p","--path", dest="exec_path", metavar="<spark_executable>", help="path to Spark executable", default="spark")
    spark_parser.add_argument("-c", "--conf", dest="platform_conf", metavar="<spark_conf>", help="specify custom Spark benchmark configuration file", default=vars.SPARK_CONF)
    spark_parser.add_argument("-d", "--detached", dest="is_detached", action="store_true", help="run the benchmark in detached mode")

    args = main_parser.parse_args()

    if args.show_version:
        print_version()

    if not args.command:
        main_parser.error("you should chose a sub-command to run")
        sys.exit(1)

    elif args.command == "build":
        build(args.tests, args.keep_files)

    else:
        try:
            kwargs = {"custom_bin_path":args.exec_path, "custom_namb_conf":args.namb_conf, "custom_platform_conf":args.platform_conf}
            if args.command == 'flink':
                kwargs["detached"] = args.is_detached
            run(args.command, **kwargs)
        except CommandNotFound as c:
            print(c)

