import pymysql

# 配置远程数据库连接
remote_config = {
    'host': 'www.yunpin.cc',
    'user': 'fgj',
    'password': 'fgj123',
    'database': 'blog',
    'charset': 'utf8mb4'
}

# 配置本地数据库连接
local_config = {
    'host': 'localhost',
    'user': 'root',
    'password': '123456',
    'database': 'myblog',
    'charset': 'utf8mb4'
}


def fetch_word_bank_data():
    # 连接远程数据库
    remote_conn = pymysql.connect(**remote_config)
    try:
        with remote_conn.cursor() as cursor:
            # 查询 word_bank 表所有数据
            sql = "SELECT * FROM word_bank"
            cursor.execute(sql)
            result = cursor.fetchall()
            return result
    finally:
        remote_conn.close()


def store_to_local(data):
    # 连接本地数据库
    local_conn = pymysql.connect(**local_config)
    try:
        with local_conn.cursor() as cursor:
            # 新增前先清空表
            cursor.execute("TRUNCATE TABLE word_bank")

            # 插入数据到本地的 word_bank 表
            sql = "INSERT INTO word_bank (column1, column2, ...) VALUES (%s, %s, ...)"
            cursor.executemany(sql, data)

        local_conn.commit()
    except Exception as e:
        print(f"Error storing data: {e}")
        local_conn.rollback()
    finally:
        local_conn.close()


if __name__ == "__main__":
    data = fetch_word_bank_data()
    if data:
        store_to_local(data)
