import os
import json

CURRENT_DIR_PATH = os.path.dirname(os.path.realpath(__file__))

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
    "son-editor/son-validate",
    "tcpser4j",
    "twirl",
    "ucloud-java-sdk",
    "UltraPlaytime",
    "wakatime-sync",
    "webbit",
    "weblaf/modules/core",
    "base-starter",
    "wechat-ssm",
    "ZingClient"
]


def load_json(file_path):
    with open(file_path, "r") as f:
        return json.load(f)


def main():
    mp = dict()
    for project_path in client_projects_path:
        vuls = load_json(os.path.join(CURRENT_DIR_PATH, project_path, 'vuls.json'))
        name = project_path.split('/')[0]
        for vul in vuls:
            if vul in mp:
                mp[vul].append(name)
            else:
                mp[vul] = [name]

    print(mp)
    with open(os.path.join(CURRENT_DIR_PATH, 'vul_app_mapping.json'), 'w') as f:
        f.write(json.dumps(mp, indent=4))

if __name__ == '__main__':
    main()
