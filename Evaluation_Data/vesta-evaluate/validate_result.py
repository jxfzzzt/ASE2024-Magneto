import json
import os
from pathlib import Path

CURRENT_DIR = os.path.dirname(__file__)
ROOT_DIR = os.path.dirname(CURRENT_DIR)
OUR_EVALUATE_RESULT_DIR = os.path.join(ROOT_DIR, 'ours-evaluate')
VUL_APP_JSON_PATH = os.path.join(ROOT_DIR, 'groundtruth', 'vul_app_mapping.json')
VESTA_RESULT_DIR = os.path.dirname(__file__)

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


def find_project(project_dir):
    for project_name in client_projects_path:
        if project_name.startswith(project_dir):
            return project_name
    return None


BAN_DIR_NAME = {'.idea', '.git', '.gitignore', 'output', '.DS_store'}


def validate_result_json(excepted_json_file, actual_json_file):
    with open(excepted_json_file, 'r') as f:
        excepted_json = json.load(f)
    with open(actual_json_file, 'r') as f:
        actual_json = json.load(f)

    excepted_json_len = len(excepted_json)
    actual_json_len = len(actual_json)

    assert excepted_json_len == actual_json_len


def validate_time_txt(json_file, time_txt):
    with open(json_file, 'r') as f:
        json_data = json.load(f)

    with open(time_txt, 'r') as f:
        time_txt_lines = f.readlines()
        r = []
        for line in time_txt_lines:
            if line:
                r.append(line)
        time_txt_lines = r

    assert len(time_txt_lines) == len(json_data)


def validate_vesta_result():
    print('vesta evaluate result:', VESTA_RESULT_DIR)
    print('total number of client project:', len(client_projects_path))

    for project_name in client_projects_path:
        our_result_project_dir = os.path.join(OUR_EVALUATE_RESULT_DIR, project_name)
        for path in Path(our_result_project_dir).iterdir():
            if path.is_dir() and path.name not in BAN_DIR_NAME:
                vul_name = path.name
                mimicry_result_dir = os.path.join(VESTA_RESULT_DIR, vul_name, project_name)
                our_result_dir = os.path.join(our_result_project_dir, vul_name)

                assert os.path.isdir(mimicry_result_dir)

                result_json_path = os.path.join(mimicry_result_dir, 'result.json')
                our_result_json_path = os.path.join(our_result_dir, 'result.json')

                assert os.path.exists(result_json_path)

                validate_result_json(our_result_json_path, result_json_path)

                time_txt_path = os.path.join(mimicry_result_dir, 'time.txt')
                assert os.path.exists(time_txt_path)

                validate_time_txt(result_json_path, time_txt_path)

    with open(VUL_APP_JSON_PATH, 'r') as f:
        vul_app_json = json.load(f)
        for vul_name, app_list in vul_app_json.items():
            app_number = len(app_list)
            assert app_number >= 1
            vul_result_dir = os.path.join(CURRENT_DIR, vul_name)
            cnt = 0
            for path in Path(str(vul_result_dir)).iterdir():
                if path.is_dir() and path.name not in BAN_DIR_NAME:
                    cnt += 1
            assert cnt == app_number

    print('vesta result validate pass!')


def main():
    validate_vesta_result()


if __name__ == '__main__':
    main()
