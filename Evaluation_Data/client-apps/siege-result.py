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



def main():
    for project_path in client_projects_path:
        result_path = os.path.join(ROOT_DIR_PATH, project_path, 'result.txt')
        print(project_path + ": ", end='')
        with open(result_path, 'r') as f:
            print(f.read())

if __name__ == '__main__':
    main()
