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


def check_success(result_json):
    for chain_list in result_json:
        if len(chain_list) > 0:
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


def resolve_result_json(vul_name, result_json_file, ground_truth_json_file):
    # Return a map with keys: success, chain, avg_len, max_len.
    with open(result_json_file, 'r') as f:
        result_json = list(json.load(f))
    with open(ground_truth_json_file, 'r') as f:
        ground_truth_json = list(json.load(f))

    ground_truth_list = []
    for ground_truth in ground_truth_json:
        vuls = ground_truth['vuls']
        if vul_name in vuls:
            ground_truth_list.append(ground_truth['chain'])

    assert len(result_json) == len(ground_truth_list)

    cnt = 0
    total_len = 0
    max_len = 0
    chain = 0

    for i in range(len(result_json)):
        cnt += 1
        chain_list = result_json[i]
        target_list = ground_truth_list[i]


        if check_list_equal(target_list, chain_list):
            total_len += len(chain_list) - 1
            max_len = max(max_len, len(chain_list) - 1)
            chain += 1

    resolve_result = dict()
    resolve_result['chian'] = chain
    resolve_result['success'] = check_success(result_json)
    resolve_result['total_len'] = total_len
    resolve_result['total_chain_cnt'] = cnt
    resolve_result['max_len'] = max_len

    return resolve_result


def evaluate_ours_tool(vul_name, app_list):
    # Return a map with keys: success, chain, avg_len, max_len.
    cnt = 0
    max_len = 0
    total_len = 0
    total_chain_cnt = 0
    success_cnt = 0
    success_chain = 0

    for app_name in app_list:
        cnt += 1
        app_complete_name = find_project(app_name)
        result_json_file = os.path.join(OUR_EVALUATE_DIR, app_complete_name, vul_name, 'result.json')
        groundtruth_json_file = os.path.join(GROUNDTRUTH_DIR, app_complete_name, 'public-call-chains.json')
        assert os.path.exists(result_json_file)
        assert os.path.exists(groundtruth_json_file)

        resolve_result = resolve_result_json(vul_name, result_json_file, groundtruth_json_file)

        success_chain += resolve_result['chian']
        success_cnt += resolve_result['success']
        total_chain_cnt += resolve_result['total_chain_cnt']
        max_len = max(max_len, resolve_result['max_len'])
        total_len += resolve_result['total_len']

    avg_len = total_len * 1.0 / total_chain_cnt

    vul_result = dict()
    vul_result['success_chain_len'] = total_len
    vul_result['total_chain_cnt'] = total_chain_cnt
    vul_result['success_cnt'] = success_cnt
    vul_result['success_chain'] = success_chain
    vul_result['total_cnt'] = cnt
    vul_result['avg_len'] = avg_len
    vul_result['max_len'] = max_len

    return vul_result


def evaluate_mimicry_tool(vul_name, app_list):
    # Return a map with keys: success, chain, avg_len, max_len.
    cnt = 0
    max_len = 0
    total_len = 0
    total_chain_cnt = 0
    success_cnt = 0
    success_chain = 0

    for app_name in app_list:
        cnt += 1
        app_complete_name = find_project(app_name)
        result_json_file = os.path.join(MIMICRY_EVALUATE_DIR, vul_name, app_complete_name, 'result.json')
        groundtruth_json_file = os.path.join(GROUNDTRUTH_DIR, app_complete_name, 'public-call-chains.json')
        assert os.path.exists(groundtruth_json_file)
        assert os.path.exists(result_json_file)

        resolve_result = resolve_result_json(vul_name, result_json_file, groundtruth_json_file)

        success_chain += resolve_result['chian']
        success_cnt += resolve_result['success']
        max_len = max(max_len, resolve_result['max_len'])
        total_len += resolve_result['total_len']
        total_chain_cnt += resolve_result['total_chain_cnt']

    avg_len = total_len * 1.0 / total_chain_cnt

    vul_result = dict()
    vul_result['success_chain_len'] = total_len
    vul_result['total_chain_cnt'] = total_chain_cnt
    vul_result['success_cnt'] = success_cnt
    vul_result['success_chain'] = success_chain
    vul_result['total_cnt'] = cnt
    vul_result['avg_len'] = avg_len
    vul_result['max_len'] = max_len

    return vul_result


def evaluate_vesta_tool(vul_name, app_list):
    # Return a map with keys: success, chain, avg_len, max_len.
    cnt = 0
    max_len = 0
    total_len = 0
    total_chain_cnt = 0
    success_cnt = 0
    success_chain = 0

    for app_name in app_list:
        cnt += 1
        app_complete_name = find_project(app_name)
        result_json_file = os.path.join(VESTA_EVALUATE_DIR, vul_name, app_complete_name, 'result.json')
        groundtruth_json_file = os.path.join(GROUNDTRUTH_DIR, app_complete_name, 'public-call-chains.json')
        assert os.path.exists(result_json_file)
        assert os.path.exists(groundtruth_json_file)

        resolve_result = resolve_result_json(vul_name, result_json_file, groundtruth_json_file)

        success_chain += resolve_result['chian']
        success_cnt += resolve_result['success']
        max_len = max(max_len, resolve_result['max_len'])
        total_len += resolve_result['total_len']
        total_chain_cnt += resolve_result['total_chain_cnt']

    avg_len = total_len * 1.0 / total_chain_cnt

    vul_result = dict()
    vul_result['success_chain_len'] = total_len
    vul_result['total_chain_cnt'] = total_chain_cnt
    vul_result['success_cnt'] = success_cnt
    vul_result['success_chain'] = success_chain
    vul_result['total_cnt'] = cnt
    vul_result['avg_len'] = avg_len
    vul_result['max_len'] = max_len

    return vul_result


def evaluate_result():
    vul_app_mapping = load_vul_app_data()

    our_result = dict()
    mimic_result = dict()
    vesta_result = dict()

    for vul_name, app_list in vul_app_mapping.items():
        our_result[vul_name] = evaluate_ours_tool(vul_name, app_list)
        mimic_result[vul_name] = evaluate_mimicry_tool(vul_name, app_list)
        vesta_result[vul_name] = evaluate_vesta_tool(vul_name, app_list)

    print('mimicry result: ')
    for vul_name, result in mimic_result.items():
        print(f'\t{vul_name}: {result}')

    print('vesta result: ')
    for vul_name, result in vesta_result.items():
        print(f'\t{vul_name}: {result}')

    print('our result: ')
    for vul_name, result in our_result.items():
        print(f'\t{vul_name}: {result}')

    print(' ============================== ')

    total_cnt = 0
    our_total_success_cnt = 0
    mimicry_total_success_cnt = 0

    for vul_name, app_list in vul_app_mapping.items():
        total_cnt += len(app_list)

    for vul_name, result in our_result.items():
        our_total_success_cnt += result['success_cnt']

    for vul_name, result in mimic_result.items():
        mimicry_total_success_cnt += result['success_cnt']

    with open(os.path.join(GROUNDTRUTH_DIR, 'vulnerabilities.json'), 'r') as f:
        vulnerabilities = json.load(f)

    print(f'vulnerability count: {len(vulnerabilities)}')
    print(f'total cnt: {total_cnt}')
    print(f'our_total_success_cnt: {our_total_success_cnt}')
    print(f'mimicry_total_success_cnt: {mimicry_total_success_cnt}')

    print(' ============================== ')

    for vul_name in vulnerabilities:
        assert vul_name in our_result.keys()
        assert vul_name in mimic_result.keys()
        assert vul_name in vesta_result.keys()

        print(
            f"{vul_name}: {mimic_result[vul_name]['success_cnt']} & {mimic_result[vul_name]['success_chain']} & {mimic_result[vul_name]['max_len']} & {mimic_result[vul_name]['avg_len']:.2f} & {vesta_result[vul_name]['success_cnt']} & {vesta_result[vul_name]['success_chain']} & {vesta_result[vul_name]['max_len']} & {vesta_result[vul_name]['avg_len']:.2f} & {our_result[vul_name]['success_cnt']} & {our_result[vul_name]['success_chain']} & {our_result[vul_name]['max_len']} & {our_result[vul_name]['avg_len']:.2f}")

    ts_mimicry = 0
    success_chain_mimicry = 0
    max_len_mimicry = 0
    total_len_mimicry = 0
    cnt_mimicry = 0
    for vul_name, result in mimic_result.items():
        ts_mimicry += result['success_cnt']
        success_chain_mimicry += result['success_chain']
        cnt_mimicry += result['total_chain_cnt']
        total_len_mimicry += result['success_chain_len']
        max_len_mimicry = max(max_len_mimicry, result['max_len'])

    avg_len_mimicry = 1.0 * total_len_mimicry / cnt_mimicry
    print(f'mimicry total: {ts_mimicry} {success_chain_mimicry} {max_len_mimicry} {total_len_mimicry} {avg_len_mimicry:.2f}')

    ts_our_vesta = 0
    success_chain_vesta = 0
    max_len_vesta = 0
    total_len_vesta = 0
    cnt_vesta = 0
    for vul_name, result in vesta_result.items():
        ts_our_vesta += result['success_cnt']
        success_chain_vesta += result['success_chain']
        max_len_vesta = max(max_len_vesta, result['max_len'])
        cnt_vesta += result['total_chain_cnt']
        total_len_vesta += result['success_chain_len']

    avg_len_vesta = 1.0 * total_len_vesta / cnt_vesta
    print(f'vesta total: {ts_our_vesta} {success_chain_vesta} {max_len_vesta} {total_len_vesta} {avg_len_vesta:.2f}')

    ts_our = 0
    success_chain_our = 0
    max_len_our = 0
    total_len_our = 0
    cnt_our = 0
    for vul_name, result in our_result.items():
        ts_our += result['success_cnt']
        success_chain_our += result['success_chain']
        max_len_our = max(max_len_our, result['max_len'])
        cnt_our += result['total_chain_cnt']
        total_len_our += result['success_chain_len']

    avg_len_our = 1.0 * total_len_our / cnt_our
    print(f'ours total: {ts_our} {success_chain_our} {max_len_our} {total_len_our} {avg_len_our:.2f}')


if __name__ == '__main__':
    evaluate_result()
