import json
import os

ROOT_DIR_PATH = os.path.dirname(__file__)

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

DEPENDENCY_RESULT_NAME = 'dependency-chain.json'
METHOD_CALLCHAIN_NAME = 'puring-method-call-chain.json'


def read_from_json_file(file_path: str):
    with open(file_path, 'r') as f:
        json_obj = json.load(f)
    return list(json_obj)


def process_dependency_chains(dependency_chains):
    s = set()
    for dependency_chain in dependency_chains:
        vul_dependency = dependency_chain[-1]
        s.add(vul_dependency)
    return s


def get_average(call_chain_list):
    sum = 0
    for call_chain in call_chain_list:
        sum += len(call_chain)
    return round(sum / len(call_chain_list), 2)


def main():
    sorted(client_projects_path)
    print(f'Total Client Project: {len(client_projects_path)}')
    for project_path in client_projects_path:
        result_path = os.path.join(ROOT_DIR_PATH, project_path, 'defender-output')
        if os.path.exists(result_path):
            try:
                dependency_result = read_from_json_file(os.path.join(result_path, DEPENDENCY_RESULT_NAME))
                callchain_result = read_from_json_file(os.path.join(result_path, METHOD_CALLCHAIN_NAME))

                # del with dependency
                print(f"{project_path}: ")
                susp_dependency_set = process_dependency_chains(dependency_result)
                print(f"  number of susp. dependency: {len(susp_dependency_set)}")
                print(f"  number of susp. callchain: {len(callchain_result)}")
                print(f"  average of susp. callchain length: {get_average(callchain_result)}")
            except Exception as e:
                print(f"[ {project_path} result not exist... ]")
        else:
            print(f"[ {project_path} result not exist... ]")


if __name__ == '__main__':
    main()
