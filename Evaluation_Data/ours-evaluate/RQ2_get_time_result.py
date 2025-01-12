import json
import os

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

assert os.path.exists(GROUNDTRUTH_DIR)
assert os.path.exists(VESTA_EVALUATE_DIR)
assert os.path.exists(MIMICRY_EVALUATE_DIR)
BAN_NAME = {'.idea', '.DS_Store', '.git', '.gitignore', 'output'}


def get_app_vul_mapping():
    with open(os.path.join(GROUNDTRUTH_DIR, 'app_vul_mapping.json'), 'r') as f:
        return json.load(f)


def find_project(project_dir):
    for project_name in client_projects_path:
        if project_name.startswith(project_dir):
            return project_name
    return None


def check_vul_exist(fuzzChain, vul_name):
    fuzzResultMap = fuzzChain['fuzzResultMap']
    s = set()
    for key, value in fuzzResultMap.items():
        s.add(key)
    return vul_name in s


# def check_chain_exploit(fuzzChain, app_complete_name):
#     gt_path = os.path.join(GROUNDTRUTH_DIR, app_complete_name, 'public-call-chains.json')
#     idx = find_prefix_chain_index(gt_path, fuzzChain['methodCallList'])
#     return idx is not None


def get_ours_time():
    fuzz_time_list = []
    cnt = 0

    app_vul_mapping = get_app_vul_mapping()
    for app_name, vul_list in app_vul_mapping.items():
        app_complete_name = find_project(app_name)
        assert app_complete_name is not None
        for vul_name in vul_list:
            cnt += 1
            report_json_path = os.path.join(OUR_EVALUATE_DIR, app_complete_name, 'output', 'report.json')
            assert os.path.exists(report_json_path)
            with open(report_json_path, 'r') as f:
                report_json = json.load(f)
            fuzzChainList = report_json['fuzzChainList']
            for fuzzChain in fuzzChainList:
                if check_vul_exist(fuzzChain, vul_name):
                    fuzz_time = fuzzChain['fuzzResultMap'][vul_name]['fuzzChainTime']
                    fuzz_time_list.append(fuzz_time)

    print(cnt)
    total_fuzz_time = sum(fuzz_time_list) * 1.0 / 1000
    avg_fuzz_time = total_fuzz_time / cnt / 60.0

    print(f'our fuzz time: {total_fuzz_time:.1f}s {avg_fuzz_time:.1f}m')


def get_vesta_time():
    time_list = []
    app_vul_mapping = get_app_vul_mapping()
    for app_name, vul_list in app_vul_mapping.items():
        app_complete_name = find_project(app_name)
        assert app_complete_name is not None
        for vul_name in vul_list:
            app_vul_time_pair = 0
            time_txt_path = os.path.join(VESTA_EVALUATE_DIR, vul_name, app_complete_name, 'time.txt')
            assert os.path.exists(time_txt_path)
            with open(time_txt_path, 'r') as f:
                for line in f.readlines():
                    if line:
                        app_vul_time_pair += int(line.strip())

            if app_vul_time_pair > 0:
                time_list.append(app_vul_time_pair)

    total_time = 0
    for i in range(len(time_list)):
        total_time += time_list[i]
    avg_time = 1.0 * total_time / len(time_list) / 60
    print(f'vesta time: {total_time}s {avg_time:.1f}m')


def get_mimicry_time():
    time_list = []
    app_vul_mapping = get_app_vul_mapping()
    for app_name, vul_list in app_vul_mapping.items():
        app_complete_name = find_project(app_name)
        assert app_complete_name is not None
        for vul_name in vul_list:
            app_vul_time_pair = 0
            time_txt_path = os.path.join(MIMICRY_EVALUATE_DIR, vul_name, app_complete_name, 'time.txt')
            assert os.path.exists(time_txt_path)
            with open(time_txt_path, 'r') as f:
                for line in f.readlines():
                    if line:
                        app_vul_time_pair += int(line.strip())

            if app_vul_time_pair > 0:
                time_list.append(app_vul_time_pair)

    total_time = 0
    for i in range(len(time_list)):
        total_time += time_list[i]

    avg_time = 1.0 * total_time / len(time_list) / 60
    print(f'mimicry time: {total_time}s {avg_time:.1f}m')


def get_time_result():
    get_ours_time()
    print('------------------')

    get_vesta_time()
    print('------------------')

    get_mimicry_time()
    print('------------------')


if __name__ == '__main__':
    get_time_result()
