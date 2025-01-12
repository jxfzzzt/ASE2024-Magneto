import multiprocessing
import os
from pathlib import Path

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
    "wechat-ssm",
    "ZingClient",
    "base-starter"
]

BAN_DIR_NAME = {'.idea', '.DS_Store'}


def work(project_dir):
    cwd = os.getcwd()
    os.chdir(project_dir)
    os.system("rm -rf evosuite-report")
    os.system("rm -rf evosuite-tests")
    os.system("rm -rf call_graph_methods.log")
    os.system("rm -rf call_graph_methods.log")
    os.system("rm -rf client_covered_goals.log")
    os.system("rm -rf functions_covered.log")
    os.system("rm -rf test.transfer_evosuite_output")
    os.system("rm -rf time_log.txt")
    os.system("rm -rf vuln_coverage_goals.log")
    os.chdir(cwd)


def get_path_from_list(client_project_name: str):
    for name in client_projects_path:
        if client_project_name in name:
            return name
    return None


def run_batch():
    for path in Path(os.path.dirname(__file__)).iterdir():
        if path.is_dir() and path.name not in BAN_DIR_NAME:
            for project_path in path.iterdir():
                if project_path.is_dir() and project_path.name not in BAN_DIR_NAME:
                    project_complete_path = get_path_from_list(project_path.name)

                    assert project_complete_path is not None

                    dest_path = os.path.join(path, project_complete_path)

                    print(dest_path)
                    assert dest_path is not None

                    work(dest_path)


if __name__ == '__main__':
    run_batch()
