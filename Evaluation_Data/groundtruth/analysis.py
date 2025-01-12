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
    print('total number of projects: {}'.format(len(client_projects_path)))
    print('current dir path:', CURRENT_DIR_PATH)

    dep_len_map = dict()
    call_len_map = dict()
    all_chain_len = []

    for project_path in client_projects_path:
        dependency_list = load_json(os.path.join(CURRENT_DIR_PATH, project_path, 'dependencies.json'))
        public_chain_list = load_json(os.path.join(CURRENT_DIR_PATH, project_path, 'public-call-chains.json'))
        private_chain_list = load_json(os.path.join(CURRENT_DIR_PATH, project_path, 'private-call-chains.json'))

        l = len(dependency_list)
        if l in dep_len_map:
            dep_len_map[l] += 1
        else:
            dep_len_map[l] = 1

        for public_chain in public_chain_list:
            l = len(public_chain)
            all_chain_len.append(l)
            if l in call_len_map:
                call_len_map[l] += 1
            else:
                call_len_map[l] = 1

        for private_chain in private_chain_list:
            l = len(private_chain)
            all_chain_len.append(l)
            all_chain_len.append(l)
            if l in dep_len_map:
                call_len_map[l] += 1
            else:
                call_len_map[l] = 1

    print('Dependency: ')
    for item in dep_len_map.items():
        print(f'\t{item[0]}: {item[1]}')

    print('Call Chain: ')
    for item in call_len_map.items():
        print(f'\t{item[0]}: {item[1]}')

    all_chain_len = sorted(all_chain_len)
    print(all_chain_len)


if __name__ == '__main__':
    main()
