import os
import csv

def get_data_from_csv():
    vul_cnt = 0
    with open('testcase-csv.csv', newline='', encoding='utf-8') as csvfile:
        csvreader = csv.reader(csvfile)
        for row in csvreader:
            if len(row) > 0:
                cve_name = row[0]
                if cve_name.startswith("CVE-"):
                    year = int(cve_name.split('-')[1])
                    if year >= 2015 and year <= 2023:
                        vul_cnt += 1

    print(vul_cnt)


if __name__ == '__main__':
    get_data_from_csv()