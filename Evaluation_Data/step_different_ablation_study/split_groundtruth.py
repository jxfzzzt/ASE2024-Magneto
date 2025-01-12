import os
import json

def process_public_call_chains(base_dir):
    # 确定目标文件夹的路径
    more_than_one_dir = os.path.join(base_dir, "step_different_ablation_study", "groundtruth_step_more_than_one")
    equal_one_dir = os.path.join(base_dir, "step_different_ablation_study", "groundtruth_step_equal_one")

    # 创建目标文件夹
    os.makedirs(more_than_one_dir, exist_ok=True)
    os.makedirs(equal_one_dir, exist_ok=True)

    # 递归搜索每个子目录并处理 public-call-chains.json
    for root, dirs, files in os.walk(os.path.join(base_dir, "groundtruth")):
        for file in files:
            if file == "public-call-chains.json":
                json_file_path = os.path.join(root, file)
                process_file(json_file_path, root, more_than_one_dir, equal_one_dir)

def process_file(json_file_path, root, more_than_one_dir, equal_one_dir):
    with open(json_file_path, 'r') as json_file:
        data = json.load(json_file)

        # 筛选并分类每个chain数据
        for entry in data:
            chain_length = len(entry['chain'])
            target_base_dir = more_than_one_dir if chain_length > 2 else equal_one_dir
            subdir = os.path.relpath(root, start=os.path.join(base_directory, "groundtruth"))
            target_folder_path = os.path.join(target_base_dir, subdir)

            # 确保目标文件夹存在
            os.makedirs(target_folder_path, exist_ok=True)

            # 目标文件路径
            target_file_path = os.path.join(target_folder_path, "public-call-chains.json")

            # 检查文件是否已存在，如果存在则读取内容
            if os.path.exists(target_file_path):
                with open(target_file_path, 'r') as file:
                    existing_data = json.load(file)
            else:
                existing_data = []

            # 添加新条目到已存在的数据
            existing_data.append(entry)

            # 写入更新后的数据到文件
            with open(target_file_path, 'w') as outfile:
                json.dump(existing_data, outfile, indent=4)

if __name__ == "__main__":
    base_directory = os.path.dirname(os.path.dirname(__file__))
    process_public_call_chains(base_directory)
