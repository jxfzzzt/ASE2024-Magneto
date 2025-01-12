import os
import json
from pathlib import Path

BAN_DIR_NAME = ['.idea', '.DS_Store']


def load_mapping():
    with open('vul_app_mapping.json', 'r') as f:
        return json.load(f)


def check():
    vul_app_mapping = load_mapping()
    print(f'total number of vulnerabilities: {len(vul_app_mapping)}')
    for path in Path(os.path.dirname(__file__)).iterdir():
        if path.is_dir() and path.name not in BAN_DIR_NAME:
            path_name = path.name

            assert path_name in vul_app_mapping

            project_set = set()
            for project_path in path.iterdir():
                if project_path.name not in BAN_DIR_NAME:
                    project_set.add(project_path.name)

            if len(vul_app_mapping[path_name]) != len(project_set):
                print(f'not equal: {path_name}')
                print(vul_app_mapping[path_name], project_set, len(vul_app_mapping[path_name]), len(project_set))

            assert len(vul_app_mapping[path_name]) == len(project_set)
    print('pass check')


if __name__ == '__main__':
    check()
