import os
import json

CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))

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

len_1_cnt = 0

def load_app_vul_mapping():
    with open('./app_vul_mapping.json', 'r') as f:
        app_vul_mapping = json.load(f)
    return app_vul_mapping


def load_vul_app_data():
    with open(os.path.join(CURRENT_DIR, 'vul_app_mapping.json'), 'r') as f:
        vul_app_mapping = json.load(f)
    return vul_app_mapping


def find_project(project_dir):
    for project_name in client_projects_path:
        if project_name.startswith(project_dir):
            return project_name
    return None


def resolve(vul_name, app_list):
    global len_1_cnt
    total_chain_number = 0
    total_project_number = 0
    max_chain_length = 0
    total_chain_length = 0

    for app_name in app_list:
        total_project_number += 1
        app_complete_name = find_project(app_name)
        assert app_complete_name is not None

        call_chain_file = os.path.join(CURRENT_DIR, app_complete_name, 'public-call-chains.json')
        assert os.path.exists(call_chain_file)
        with open(call_chain_file, 'r') as f:
            call_chain_data = json.load(f)

        for call_chain in call_chain_data:
            if vul_name in call_chain['vuls']:
                total_chain_number += 1
                chain = list(call_chain['chain'])
                assert len(chain) > 0
                if len(chain) - 1 == 1:
                    len_1_cnt += 1
                total_chain_length += len(chain) - 1
                max_chain_length = max(max_chain_length, len(chain) - 1)

    result = dict()
    result['total_chain_number'] = total_chain_number
    result['total_project_number'] = total_project_number
    result['max_chain_length'] = max_chain_length
    result['avg_chain_length'] = total_chain_length * 1.0 / total_chain_number
    result['total_chain_length'] = total_chain_length
    return result


def get_groundtruth_data():
    global len_1_cnt
    vul_app_mapping = load_vul_app_data()
    ground_data = dict()

    for vul_name, app_list in vul_app_mapping.items():
        d = resolve(vul_name, app_list)
        assert d is not None
        ground_data[vul_name] = d

    print(' ============================== ')

    with open(os.path.join(CURRENT_DIR, 'vulnerabilities.json'), 'r') as f:
        vulnerabilities = json.load(f)

    print(f'{len(client_projects_path)} {len(vulnerabilities)}')
    total_project = 0
    total_chain = 0
    total_chain_length = 0
    max_chain_len = 0
    for vul_name in vulnerabilities:
        assert vul_name in ground_data.keys()
        res = ground_data[vul_name]
        total_project += res['total_project_number']
        total_chain += res['total_chain_number']
        max_chain_len = max(max_chain_len, res['max_chain_length'])
        print(
            f"{vul_name}: {res['total_project_number']} & {res['total_chain_number']} & {res['max_chain_length']} & {res['avg_chain_length']:.2f}")
        total_chain_length += res['total_chain_length']

    avg_len = total_chain_length * 1.0 / total_chain
    print(f'total: {total_project} {total_chain} {total_chain_length} {max_chain_len} {avg_len:.2f}')

    project_vulnerability_pair = 0
    for app_name, vul_list in load_app_vul_mapping().items():
        project_vulnerability_pair += len(vul_list)

    print(f'project vulnerability pair: {project_vulnerability_pair}')
    print('the number of chain length is 1: ', len_1_cnt)
    return ground_data


if __name__ == '__main__':
    get_groundtruth_data()
