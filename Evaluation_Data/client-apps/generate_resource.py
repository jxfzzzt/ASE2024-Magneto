import json
import os
import shutil

CURRENT_DIR = os.path.dirname(__file__)
GROUNDTRUTH_DIR = '/root/TestCase_Collect/java/3-testcase-trigger/groundtruth'
TARGET_DIR = '/root/resource'

if not os.path.exists(GROUNDTRUTH_DIR):
    exit(0)

if not os.path.exists(TARGET_DIR):
    exit(0)

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


def find_complete_project_name(name):
    for project_name in client_projects_path:
        if project_name.startswith(name):
            return project_name
    return None


def get_vulnerabilities():
    with open(os.path.join(GROUNDTRUTH_DIR, 'vulnerabilities.json'), 'r') as f:
        vulnerabilities = json.load(f)
    return vulnerabilities


def get_vul_app_mapping():
    with open(os.path.join(GROUNDTRUTH_DIR, 'vul_app_mapping.json'), 'r') as f:
        vul_app_mapping = json.load(f)
    return vul_app_mapping

### for VESTA
if __name__ == '__main__':
    vulnerabilities = get_vulnerabilities()
    vul_app_mapping = get_vul_app_mapping()

    ### copy client project
    for vul_name in vulnerabilities:
        print(vul_name)
        assert vul_name in vul_app_mapping.keys()
        app_list = vul_app_mapping[vul_name]
        for app_name in app_list:
            target_project_dir = os.path.join(TARGET_DIR, vul_name, app_name)
            source_project_dir = os.path.join(CURRENT_DIR, app_name)

            print(f'\t{app_name}: {source_project_dir} ----> {target_project_dir}')
            # 如果目标文件夹存在，则跳过
            if os.path.exists(target_project_dir):
                continue

            # 复制文件夹
            shutil.copytree(str(source_project_dir), target_project_dir, dirs_exist_ok=True)

    ### remove the test file
    for vul_name in vulnerabilities:
        assert vul_name in vul_app_mapping.keys()
        app_list = vul_app_mapping[vul_name]
        for app_name in app_list:
            project_dir = os.path.join(TARGET_DIR, vul_name, find_complete_project_name(app_name))
            assert os.path.exists(project_dir)
            test_dir = os.path.join(project_dir, 'src', 'test')
            if os.path.exists(test_dir):
                shutil.rmtree(test_dir)
