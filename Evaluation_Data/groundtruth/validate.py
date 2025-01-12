import json
import os

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
    print('total number of projects: {}'.format(len(client_projects_path)))
    print('current dir path:', CURRENT_DIR_PATH)

    for project_path in client_projects_path:
        dependency_list = load_json(os.path.join(CURRENT_DIR_PATH, project_path, 'dependencies.json'))
        public_chain_list = load_json(os.path.join(CURRENT_DIR_PATH, project_path, 'public-call-chains.json'))
        private_chain_list = load_json(os.path.join(CURRENT_DIR_PATH, project_path, 'private-call-chains.json'))

        for dependency in dependency_list:
            assert len(dependency) != 0

        for public_chain in public_chain_list:
            chain = public_chain['chain']
            vuls = public_chain['vuls']
            assert len(chain) != 0
            assert len(vuls) != 0

        for private_chain in private_chain_list:
            chain = private_chain['chain']
            vuls = private_chain['vuls']
            assert len(chain) != 0
            assert len(vuls) != 0

    print('validation complete: success!')


if __name__ == '__main__':
    main()
