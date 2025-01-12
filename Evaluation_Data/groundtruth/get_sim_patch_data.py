import os
import json


def get_sim_patch_data():
    with open('sim_patch.json', 'r') as f:
        patch_data = json.load(f)

    vul_list = []
    for vul_name, vul_data in patch_data.items():
        year = int(vul_name.split('-')[1])
        if year >= 2015 and year <= 2023:
            vul_list.append(vul_name)

    print(len(vul_list))
    print(vul_list)


if __name__ == '__main__':
    get_sim_patch_data()
