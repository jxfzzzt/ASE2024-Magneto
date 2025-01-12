import os
import json

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

CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))


def get_groundtruth_data():
    total_chain_number = 0
    max_chain_length = 0
    total_chain_length = 0

    for client_project in client_projects_path:
        data_dir = os.path.join(CURRENT_DIR, client_project, 'public-call-chains.json')
        assert os.path.exists(data_dir)
        with open(data_dir, 'r') as f:
            data = json.load(f)
            for chain_data in data:
                total_chain_length += (len(chain_data['chain']) - 1) * len(chain_data['vuls'])
                max_chain_length = max(max_chain_length, len(chain_data['chain']) - 1)
                total_chain_number += len(chain_data['vuls'])

    print(f'Max chain length: {max_chain_length}')
    print(f'Total number of exploit call chains: {total_chain_number}')

    avg_len = 1.0 * total_chain_length / total_chain_number
    print(f'average number of exploit call chains: {avg_len:.2f}')

    length_one_cnt = 0
    for client_project in client_projects_path:
        data_dir = os.path.join(CURRENT_DIR, client_project, 'public-call-chains.json')
        assert os.path.exists(data_dir)
        with open(data_dir, 'r') as f:
            data = json.load(f)
            for chain_data in data:
                if len(chain_data['chain']) == 2:
                    length_one_cnt += len(chain_data['vuls'])

    print(f'The number of length one chain: {length_one_cnt}')


def get_vulnerabilities():
    with open('vulnerabilities.json', 'r') as f:
        data = json.load(f)
    return data


TRANSFER_VULNERABILITIES = [
    "CVE-2020-28052",
    "CODEC-134",
    "CVE-2020-13956",
    "HTTPCLIENT-1803",
    "CVE-2019-14900",
    "CVE-2020-15250",
    "CVE-2021-23899",
    "CVE-2020-26217",
    "CVE-2019-12415",
    "CVE-2018-1000632",
    "CVE-2020-10693",
    "CVE-2018-1000873",
    "CVE-2019-12402",
    "CVE-2018-12418",
    "CVE-2019-10094",
    "TwelveMonkeys-595",
    "CVE-2020-28491",
    "CVE-2018-1274",
    "CVE-2021-27568",
    "Zip4J-263",
    "Spring Security-8317",
    "CVE-2017-7957"
]

VESTA_VULNERABILITIES = [
    "CVE-2017-7957",
    "CVE-2021-39144",
    "CVE-2021-21341",
    "CVE-2022-41966",
    "CVE-2020-26217",
    "CODEC-263",
    "CODEC-270",
    "TEXT-215",
    "CVE-2022-42889",
    "LANG-1484",
    "LANG-1645",
    "LANG-1385",
    "CVE-2023-1370",
    "CVE-2021-27568",
    "CVE-2022-45688",
    "CVE-2019-14540",
    "CVE-2019-12415",
    "CVE-2022-24615",
    "Zip-263",
    "IO-611",
    "CVE-2021-29425",
    "CVE-2021-31812",
    "CVE-2021-35516",
    "CVE-2018-1324",
    "CVE-2020-15250",
    "CVE-2022-22965",
    "CVE-2020-13956",
    "CVE-2021-37714",
    "CVE-2021-44228",
    "CVE-2019-14900"
]


def get_exploit_cve_list():
    with open('exploit_cve_list.json', 'r') as f:
        data = json.load(f)
        return data

def get_source_distribute():
    vulnerabilities = get_vulnerabilities()
    self_collect_cve = 0
    exploit_cve_list = get_exploit_cve_list()
    for cve_name in exploit_cve_list:
        if cve_name in vulnerabilities:
            self_collect_cve += 1

    print('we collected {} from exploit cves'.format(self_collect_cve))

    print('There are {} vulnerabilities in our dataset'.format(len(vulnerabilities)))

    ### Transfer
    in_transfer = 0
    for vul_name in vulnerabilities:
        if vul_name in TRANSFER_VULNERABILITIES:
            in_transfer += 1
    print(f'The number of vulnerabilities in Transfer: {len(TRANSFER_VULNERABILITIES)}. {in_transfer} are same')

    ### VESTA
    in_vesta = 0
    for vul_name in vulnerabilities:
        if vul_name in VESTA_VULNERABILITIES:
            in_vesta += 1
    print(f'The number of vulnerabilities in VESTA: {len(VESTA_VULNERABILITIES)}. {in_vesta} are same')

if __name__ == '__main__':
    get_groundtruth_data()
    get_source_distribute()
