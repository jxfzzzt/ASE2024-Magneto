import json
import os


def get_app_vul_mapping():
    ret = dict()
    with open('vul_app_mapping.json', 'r') as f:
        vul_app_mapping = json.load(f)
        for vul_name, app_list in vul_app_mapping.items():
            for app_name in app_list:
                if app_name not in ret:
                    ret[app_name] = set()
                ret[app_name].add(vul_name)

    for app_name, vul_list in ret.items():
        ret[app_name] = sorted(list(vul_list))

    with open('app_vul_mapping.json', 'w') as f:
        f.write(json.dumps(ret))

    return ret


if __name__ == '__main__':
    app_vul_mapping = get_app_vul_mapping()
    print(app_vul_mapping)
