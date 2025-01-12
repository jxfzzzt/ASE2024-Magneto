import os
import json
from pathlib import Path

BAN_FILE = {'.idea', 'target', '.idea', '.git'}
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))


def get_vulnerabilities():
    with open('vulnerabilities.json', 'r') as f:
        vul_list = json.load(f)
    return vul_list

def calc_data():
    vul_list = get_vulnerabilities()
    print(f'The number of vulnerabilities: {len(vul_list)}')

    vul_method_cnt = 0
    testcase_cnt = 0

    for vul_name in vul_list:
        metainfo_dir = os.path.join(CURRENT_DIR, vul_name, 'metainfo.json')
        assert os.path.exists(metainfo_dir)
        with open(metainfo_dir, 'r') as f:
            metainfo = json.load(f)
            s = set()
            exploits = metainfo['testcases']
            testcase_cnt += len(exploits)
            for exploit in exploits:
                s.add(exploit['vulMethodSignature'])
            vul_method_cnt += len(s)

    print(f'The total number of vulnerable method: {vul_method_cnt}')
    print(f'The total number of vulnerable testcase: {testcase_cnt}')

if __name__ == '__main__':
    calc_data()
