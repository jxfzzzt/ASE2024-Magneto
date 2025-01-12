import os
import json
import shutil
from pathlib import Path

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


def process_public_call_chains(base_dir):
    gpt_scope_dir = os.path.join(base_dir, "gpt-scope-4")
    step_more_than_one_dir = os.path.join(base_dir, "step_different_ablation_study", "gpt-scope-4-step-more-than-one")
    step_equal_one_dir = os.path.join(base_dir, "step_different_ablation_study", "gpt-scope-4-step-equal-one")

    ground_truth_path = os.path.join(base_dir, 'groundtruth')

    os.makedirs(step_more_than_one_dir, exist_ok=True)
    os.makedirs(step_equal_one_dir, exist_ok=True)

    for project_name in client_projects_path:
        with open(os.path.join(ground_truth_path, project_name, 'public-call-chains.json')) as f:
            gt_data = json.load(f)

        vuls_json_file = os.path.join(ground_truth_path, project_name, 'vuls.json')
        with open(vuls_json_file, 'r') as f:
            vuls = json.load(f)
        for vul_name in vuls:
            source_file = os.path.join(gpt_scope_dir, project_name, vul_name, 'result.json')
            assert os.path.exists(source_file)

            with open(source_file, 'r') as f:
                source_evaluate_list = json.load(f)

            source_list = []

            for gt in gt_data:
                if vul_name in gt['vuls']:
                    source_list.append(gt['chain'])

            assert len(source_list) == len(source_evaluate_list)
            chain_step_equal_one = []
            chain_step_more_than_one = []

            for i in range(len(source_list)):
                if len(source_list[i]) > 2:
                    chain_step_more_than_one.append(source_evaluate_list[i])
                else:
                    chain_step_equal_one.append(source_evaluate_list[i])

            target_step_equal_one_path = os.path.join(step_equal_one_dir, project_name, vul_name, 'result.json')
            target_step_more_than_one_path = os.path.join(step_more_than_one_dir, project_name, vul_name, 'result.json')

            with open(target_step_equal_one_path, 'w') as f:
                f.write(json.dumps(chain_step_equal_one))

            with open(target_step_more_than_one_path, 'w') as f:
                f.write(json.dumps(chain_step_more_than_one))

if __name__ == "__main__":
    base_directory = os.path.dirname(os.path.dirname(__file__))
    process_public_call_chains(base_directory)
