import os
import json


ROOT_DIR = os.path.dirname(__file__)
GROUNDTRUTH_DIR = os.path.join(ROOT_DIR, 'groundtruth-step-equal-one')
gpt4_dir = os.path.join(ROOT_DIR, 'ours-evaluate-step-equal-one')
gpt3_dir = os.path.join(ROOT_DIR, 'ours-evaluate-gpt3.5-step-equal-one')
wo_seed_template_dir = os.path.join(ROOT_DIR, 'ours-evaluate-wogpt-step-equal-one')
wo_directed_fuzzing_dir = os.path.join(ROOT_DIR, 'ours-wo-mutation-step-equal-one')
wo_connect_seed_synthesis_dir = os.path.join(ROOT_DIR, 'ours-wo-connect-step-equal-one')
scope_1_dir = os.path.join(ROOT_DIR, 'gpt-scope-1-step-equal-one')
scope_3_dir = os.path.join(ROOT_DIR, 'gpt-scope-3-step-equal-one')
scope_4_dir = os.path.join(ROOT_DIR, 'gpt-scope-4-step-equal-one')

assert os.path.exists(gpt4_dir)
assert os.path.exists(gpt3_dir)
assert os.path.exists(wo_seed_template_dir)
assert os.path.exists(wo_directed_fuzzing_dir)
assert os.path.exists(wo_connect_seed_synthesis_dir)
assert os.path.exists(scope_1_dir)
assert os.path.exists(scope_3_dir)
assert os.path.exists(scope_4_dir)

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


def get_cves_by_app_name(app_name):
    vuls_file_path = os.path.join(GROUNDTRUTH_DIR, app_name, 'vuls.json')
    assert vuls_file_path
    with open(vuls_file_path, 'r') as f:
        vuls_data = json.load(f)
    return vuls_data


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


def resolve_result_json(vul_name, result_data_path, ground_truth_json_file):
    with open(result_data_path, 'r') as f:
        result_data = json.load(f)
    with open(ground_truth_json_file, 'r') as f:
        ground_truth_json = list(json.load(f))

    ground_truth_list = []
    for ground_truth in ground_truth_json:
        vuls = ground_truth['vuls']
        if vul_name in vuls:
            ground_truth_list.append(ground_truth['chain'])

    if len(result_data) != len(ground_truth_list):
        print(result_data_path)
        print(ground_truth_json_file)
    assert len(result_data) == len(ground_truth_list)

    res = dict()
    total_chain = 0
    chain_len = 0
    max_chain_len = 0
    success_chain = 0

    for i in range(len(result_data)):
        total_chain += 1
        chain = result_data[i]
        target_list = ground_truth_list[i]

        if check_list_equal(target_list, chain):
            chain_len += len(chain) - 1
            max_chain_len = max(max_chain_len, len(chain) - 1)
            success_chain += 1

    res['success'] = check_success(result_data)
    res['total_chain_cnt'] = total_chain
    res['success_chain'] = success_chain
    res['chain_len'] = chain_len
    res['max_chain_len'] = max_chain_len

    return res


def get_ablation_result(data_dir):
    res = dict()

    chain_len = 0
    max_chain_len = 0

    res['success_cnt'] = 0
    res['total_chain_cnt'] = 0
    res['max_chain_len'] = 0
    res['success_chain'] = 0

    for project_name in client_projects_path:
        vuls_data = get_cves_by_app_name(project_name)
        for vul_name in vuls_data:
            result_data_path = os.path.join(data_dir, project_name, vul_name, 'result.json')
            ground_truth_json_file = os.path.join(GROUNDTRUTH_DIR, project_name, 'public-call-chains.json')
            assert os.path.exists(result_data_path)
            assert os.path.exists(ground_truth_json_file)

            # 空就直接跳过
            with open(ground_truth_json_file, 'r') as f:
                g_data = json.load(f)
            if not g_data:
                continue

            d = resolve_result_json(vul_name, result_data_path, ground_truth_json_file)

            res['total_chain_cnt'] += d['total_chain_cnt']
            res['success_chain'] += d['success_chain']
            res['success_cnt'] += d['success']
            chain_len += d['chain_len']
            max_chain_len = max(max_chain_len, d['max_chain_len'])


    res['avg_chain_len'] = chain_len * 1.0 / res['total_chain_cnt']
    res['max_chain_len'] = max_chain_len
    return res


def main():
    print(f'total number of client projects: {len(client_projects_path)}')


    gpt4_result = get_ablation_result(gpt4_dir)
    print(f'gpt4: {gpt4_result["success_cnt"]} {gpt4_result["success_chain"]} {gpt4_result["avg_chain_len"]:.2f} {gpt4_result["max_chain_len"]}')

    gpt3_result = get_ablation_result(gpt3_dir)
    print(f'gpt3: {gpt3_result["success_cnt"]} {gpt3_result["success_chain"]} {gpt3_result["avg_chain_len"]:.2f} {gpt3_result["max_chain_len"]}')

    wo_seed_template_result = get_ablation_result(wo_seed_template_dir)
    print(f'wo-seed-template: {wo_seed_template_result["success_cnt"]} {wo_seed_template_result["success_chain"]} {wo_seed_template_result["avg_chain_len"]:.2f} {wo_seed_template_result["max_chain_len"]}')

    wo_directed_fuzzing_result = get_ablation_result(wo_directed_fuzzing_dir)
    print(f'wo-directed-fuzzing: {wo_directed_fuzzing_result["success_cnt"]} {wo_directed_fuzzing_result["success_chain"]} {wo_directed_fuzzing_result["avg_chain_len"]:.2f} {wo_directed_fuzzing_result["max_chain_len"]}')

    wo_connect_seed_synthesis = get_ablation_result(wo_connect_seed_synthesis_dir)
    print(f'wo-connect-seed-synthesis: {wo_connect_seed_synthesis["success_cnt"]} {wo_connect_seed_synthesis["success_chain"]} {wo_connect_seed_synthesis["avg_chain_len"]:.2f} {wo_connect_seed_synthesis["max_chain_len"]}')

    scope_1 = get_ablation_result(scope_1_dir)
    print(f'scope-1: {scope_1["success_cnt"]} {scope_1["success_chain"]} {scope_1["avg_chain_len"]:.2f} {scope_1["max_chain_len"]}')

    scope_3 = get_ablation_result(scope_3_dir)
    print(f'scope-3: {scope_3["success_cnt"]} {scope_3["success_chain"]} {scope_3["avg_chain_len"]:.2f} {scope_3["max_chain_len"]}')

    scope_4 = get_ablation_result(scope_4_dir)
    print(f'scope-4: {scope_4["success_cnt"]} {scope_4["success_chain"]} {scope_4["avg_chain_len"]:.2f} {scope_4["max_chain_len"]}')


if __name__ == '__main__':
    main()
