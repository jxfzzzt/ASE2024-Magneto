import os
import re
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
    "wechat-ssm",
    "ZingClient",
    "base-starter"
]

BAN_DIR_NAME = {'.idea', '.DS_Store'}


def get_path_from_list(client_project_name: str):
    for name in client_projects_path:
        if client_project_name in name:
            return name
    return None


def init_target():
    client_apps_dir = os.path.join(os.path.dirname(os.path.dirname(__file__)), "client-apps")

    assert os.path.exists(client_apps_dir)

    for path in Path(os.path.dirname(__file__)).iterdir():
        if path.is_dir() and path.name not in BAN_DIR_NAME:
            print(f'current process: {path.name}')

            for project_path in path.iterdir():
                if project_path.is_dir() and project_path.name not in BAN_DIR_NAME:
                    project_complete_path = get_path_from_list(project_path.name)

                    assert project_complete_path is not None
                    print(project_complete_path)

                    target_path = os.path.join(client_apps_dir, project_complete_path, "target")


                    dest_path = os.path.join(path, project_complete_path, "target")

                    assert dest_path is not None

                    print(f'[{target_path} --> {dest_path}]')
                    shutil.copytree(target_path, dest_path, dirs_exist_ok=True)


if __name__ == '__main__':
    init_target()
