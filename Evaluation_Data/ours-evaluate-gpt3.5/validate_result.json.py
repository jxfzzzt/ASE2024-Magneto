import os
import json
from pathlib import Path

CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
EVALUATE_DIR = os.path.join(os.path.dirname(CURRENT_DIR), 'ours-evaluate')

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
    "wakatime-sync",
    "WxJava/weixin-java-mp",
    "webbit",
    "weblaf/modules/core",
    "base-starter",
    "wechat-ssm",
    "ZingClient"
]

BAN_DIR_NAME = ['.idea', 'output', '.DS_Store']


def get_result(result_file_path):
    with open(result_file_path, 'r') as f:
        result = json.load(f)
    return result


def validate_result():
    print('total number of client projects:', len(client_projects_path))
    for project_name in client_projects_path:
        project_dir = os.path.join(CURRENT_DIR, project_name)
        for path in Path(str(project_dir)).iterdir():
            if path.is_dir() and path.name not in BAN_DIR_NAME:
                vul_name = path.name
                cur_result_path = os.path.join(CURRENT_DIR, project_name, vul_name, 'result.json')
                pre_result_path = os.path.join(EVALUATE_DIR, project_name, vul_name, 'result.json')

                assert os.path.exists(cur_result_path)
                assert os.path.exists(pre_result_path)

                cur_result = get_result(cur_result_path)
                pre_result = get_result(pre_result_path)

                assert len(cur_result) == len(pre_result)

    print('validate result success')


if __name__ == '__main__':
    validate_result()
