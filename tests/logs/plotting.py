import glob
import os
import re
import pandas as pd
import matplotlib.pyplot as plt

def boxplot(root, opts = {}):
    bw = 'busywait' in root
    cpu_to_plot = {}
    re_pattern = '([0-9]*)_([a-z]+?|[a-z]+_[0-9]+?)_bolt_'
    if bw:
        sorted_files = [f for f in sorted(glob.glob(os.path.join(root, "*.csv")), 
                                    key=lambda k: int( re.search(re_pattern, k).group(2).replace("busywait_","") ))]
    else:
        sorted_files = [f for f in sorted(glob.glob(os.path.join(root, "*.csv")), 
                                    key=lambda k: re.search(re_pattern, k).group(2))]
    
    for csv in sorted_files:
        
        task_name = re.search(re_pattern, csv).group(2)
        df = pd.read_csv(csv, sep=',')
        cpu_time = df["tot"] \
            .diff() \
            .divide(1000000) \
            .iloc[1:] \
            .tolist() 
        cpu_to_plot[task_name] = cpu_time
    

    labels, data = cpu_to_plot.keys(), cpu_to_plot.values()
    if bw: labels = [l.replace("busywait_", "") for l in labels]
    plt.boxplot(data)
    plt.xticks(range(1, len(labels) + 1), labels)
    plt.ylabel("CPU Load (ms)")
    if bw: plt.xlabel("x1000 cycles")
    plt.title(root.replace("_", " "))

    output_name = "{}_boxplot.{}".format(root, opts["img_format"])
    if "out" in opts: plt.savefig(os.path.join(opts["out"], output_name))
    else: plt.show()
    plt.close('all')
