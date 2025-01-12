import os
import json
from pathlib import Path

OUR_EVALUATE_DIR = os.path.dirname(__file__)
GROUNDTRUTH_DIR = os.path.join(os.path.dirname(OUR_EVALUATE_DIR), 'groundtruth')
MIMICRY_EVALUATE_DIR = os.path.join(os.path.dirname(OUR_EVALUATE_DIR), 'mimicry-evaluate')
VESTA_EVALUATE_DIR = os.path.join(os.path.dirname(OUR_EVALUATE_DIR), 'vesta-evaluate')
client_projects_path = [
    "adu-test",
    "adyen-api",
    "archivefs",
    "axon-server-se/axonserver",
    "bean-query",
    "CarStoreApi/account/account-web",
    "commons-validator",
    "db/engine",
    "elasticsearch-maven-plugin",
    "flow",
    "geek-framework",
    "gerenciador-viagens",
    "huntfiles",
    "idworker",
    "jerry-core",
    "jfinal",
    "JsonConfiguration",
    "kafka-keyvalue",
    "karate/karate-core",
    "knetbuilder/ondex-base/core/marshal",
    "mirage/mirage-core",
    "Mixmicro-Components/llc-kits",
    "neo",
    "ninja/ninja-core",
    "OmegaTester",
    "Online_Train_Ticket_Reservation_System/Code",
    "PatentPublicData/Common",
    "pdf-converter",
    "pdf-util",
    "PLMCodeTemplate/source",
    "QLExpress",
    "reproducible-build-maven-plugin",
    "rike/arago-rike-commons",
    "roubsite/RoubSiteUtils",
    "rpki-commons",
    "RuoYi-Vue-Multi-Tenant/multi-tenant-server",
    "serritor",
    "son-editor/son-validate-web",
    "tcpser4j",
    "twirl",
    "ucloud-java-sdk",
    "UltraPlaytime",
    "WxJava/weixin-java-mp",
    "wakatime-sync",
    "webbit",
    "weblaf/modules/core",
    "base-starter",
    "wechat-ssm",
    "ZingClient"
]
BAN_NAME = {'.idea', '.DS_Store', '.git', '.gitignore', 'output'}

assert os.path.exists(GROUNDTRUTH_DIR)
assert os.path.exists(OUR_EVALUATE_DIR)
assert os.path.exists(MIMICRY_EVALUATE_DIR)
assert os.path.exists(VESTA_EVALUATE_DIR)

print(f'GROUNDTRUTH DIR: {GROUNDTRUTH_DIR}')
print(f'our evaluate dir: {OUR_EVALUATE_DIR}')
print(f'mimicry evaluate dir: {MIMICRY_EVALUATE_DIR}')
print(f'vesta evaluate dir: {VESTA_EVALUATE_DIR}')


def load_vul_app_data():
    with open(os.path.join(GROUNDTRUTH_DIR, 'vul_app_mapping.json'), 'r') as f:
        vul_app_mapping = json.load(f)
    return vul_app_mapping


def find_project(project_dir):
    for project_name in client_projects_path:
        if project_name.startswith(project_dir):
            return project_name
    return None


def check_has_target_len(result_json_path, target_len):
    with open(result_json_path, 'r') as f:
        chain_list = json.load(f)
        for chain in chain_list:
            if len(chain) > 0 and len(chain) - 1 >= target_len:
                return True
    return False

def check_list_equal(list1, list2):
    if len(list1) != len(list2):
        return False

    assert len(list1) == len(list2)
    for i in range(len(list1)):
        if list1[i] != list2[i]:
            return False

    return True

def get_target_chain_length_count(result_json_path, target_len):
    chain_count = 0
    with open(result_json_path, 'r') as f:
        chain_list = json.load(f)
        for chain in chain_list:
            if len(chain) > 0 and len(chain) - 1 >= target_len:
                chain_count += 1
    return chain_count


def get_mimicry_length_result():
    result_map = dict()
    for i in range(1, 7):
        result_map[i] = dict({
            'cve': set(),
            'project': 0,
            'chain_number': 0
        })

    for i in range(1, 7):
        vul_app_mapping = load_vul_app_data()
        for vul_name, app_list in vul_app_mapping.items():
            for app_name in app_list:
                app_complete_name = find_project(app_name)
                assert app_complete_name is not None
                result_json_path = os.path.join(MIMICRY_EVALUATE_DIR, vul_name, app_complete_name, 'result.json')
                assert os.path.exists(result_json_path)
                if check_has_target_len(result_json_path, i):
                    result_map[i]['cve'].add(vul_name)
                    target_len_count = get_target_chain_length_count(result_json_path, i)
                    result_map[i]['project'] += 1
                    result_map[i]['chain_number'] += target_len_count

    print('mimicry length result:')
    for key, value in result_map.items():
        cve_count = len(value['cve'])
        project_count = value['project']
        chain_count = value['chain_number']
        print(f'\t{key}: {cve_count} {project_count} {chain_count}')


def get_vesta_length_result():
    result_map = dict()
    for i in range(1, 7):
        result_map[i] = dict({
            'cve': set(),
            'project': 0,
            'chain_number': 0
        })
    for i in range(1, 7):
        vul_app_mapping = load_vul_app_data()
        for vul_name, app_list in vul_app_mapping.items():
            for app_name in app_list:
                app_complete_name = find_project(app_name)
                assert app_complete_name is not None
                result_json_path = os.path.join(VESTA_EVALUATE_DIR, vul_name, app_complete_name, 'result.json')
                assert os.path.exists(result_json_path)
                if check_has_target_len(result_json_path, i):
                    result_map[i]['cve'].add(vul_name)
                    target_len_count = get_target_chain_length_count(result_json_path, i)
                    result_map[i]['project'] += 1
                    result_map[i]['chain_number'] += target_len_count

    print('vesta length result:')
    for key, value in result_map.items():
        cve_count = len(value['cve'])
        project_count = value['project']
        chain_count = value['chain_number']
        print(f'\t{key}: {cve_count} {project_count} {chain_count}')


def get_ours_length_result():
    result_map = dict()
    for i in range(1, 7):
        result_map[i] = dict({
            'cve': set(),
            'project': 0,
            'chain_number': 0
        })
    for i in range(1, 7):
        for project_name in client_projects_path:
            project_dir = os.path.join(OUR_EVALUATE_DIR, project_name)
            for path in Path(project_dir).iterdir():
                if path.is_dir() and path.name not in BAN_NAME:
                    vul_name = path.name
                    result_json_path = os.path.join(project_dir, vul_name, 'result.json')
                    assert os.path.exists(result_json_path)
                    if check_has_target_len(result_json_path, i):
                        result_map[i]['cve'].add(vul_name)
                        target_len_count = get_target_chain_length_count(result_json_path, i)
                        result_map[i]['project'] += 1
                        result_map[i]['chain_number'] += target_len_count

    print('ours length result:')
    for key, value in result_map.items():
        cve_count = len(value['cve'])
        project_count = value['project']
        chain_count = value['chain_number']
        print(f'\t{key}: {cve_count} {chain_count} {project_count}')


def get_compare_length_result():
    print(' ------------------------------')
    get_mimicry_length_result()

    print(' ------------------------------')
    get_vesta_length_result()

    print(' ------------------------------')
    get_ours_length_result()


if __name__ == '__main__':
    get_compare_length_result()
