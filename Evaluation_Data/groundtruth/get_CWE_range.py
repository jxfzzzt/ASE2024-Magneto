import json


def get_vulnerabilities():
    with open('vulnerabilities.json', 'r') as f:
        vulnerabilities = json.load(f)
    return vulnerabilities


def get_cve_cwe_map():
    with open('cve_cwe_map.json', 'r') as f:
        cve_cwe_map = json.load(f)
    return cve_cwe_map


def get_cwe_range():
    vulnerabilities = get_vulnerabilities()
    cve_cwe_map = get_cve_cwe_map()

    cwe_set = set()

    for vul_name in vulnerabilities:
        if vul_name.startswith('CVE'):
            if vul_name in cve_cwe_map:
                cwe_set.add(cve_cwe_map[vul_name])

    cwe_list = list(cwe_set)
    print('CWE list:', cwe_list)
    print('The cwe range is ' + str(len(cwe_list)))


if __name__ == '__main__':
    get_cwe_range()
