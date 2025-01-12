import json


def match(chain_seq, seq):
    if len(chain_seq) < len(seq):
        return False

    for i in range(len(seq)):
        if seq[len(seq) - i - 1] != chain_seq[len(chain_seq) - i - 1]:
            return False
    return True


def find_prefix_chain_index(chain_json_file, seq):
    with open(chain_json_file, 'r') as f:
        chain_data = json.load(f)

    for idx, chain in enumerate(chain_data):
        chain_seq = list(chain['chain'])
        if len(chain_seq) < len(seq):
            continue
        if match(chain_seq, seq):
            return idx
    return None

