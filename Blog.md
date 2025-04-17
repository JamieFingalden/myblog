---
title: Blog
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
code_clipboard: true
highlight_theme: darkula
headingLevel: 2
generator: "@tarslib/widdershins v4.0.30"

---

# Blog

Base URLs:

# Authentication

# Posts

## GET 查询所有文章

GET /posts/list

> 返回示例

> 200 Response

```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "title": "博客后端连接测试",
      "content": "查看内容是否成功响应",
      "excerpt": "null",
      "status": "草稿",
      "createdAt": null,
      "publishedAt": null
    },
    {
      "id": 2,
      "title": "文章标题",
      "content": "文章内容",
      "excerpt": "节选自",
      "status": "完成",
      "createdAt": null,
      "publishedAt": null
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none||none|
|» msg|string|true|none||none|
|» data|[object]|true|none||none|
|»» id|integer|false|none||none|
|»» title|string|false|none||none|
|»» content|string|false|none||none|
|»» excerpt|string|false|none||none|
|»» status|string|false|none||none|
|»» createdAt|null|false|none||none|
|»» publishedAt|null|false|none||none|

## POST 新增博客文章

POST /posts

"title":"文章标题",
"content":"文章内容",
"excerpt":"节选自可为空",
"status":"完成或草稿，默认为草稿"

> Body 请求参数

```json
{
  "title": "文章标题",
  "content": "文章内容",
  "excerpt": "节选自",
  "status": "完成"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT 修改文章

PUT /posts

> Body 请求参数

```json
{
  "id": 2,
  "title": "博客后端连接测试2",
  "content": "查看内容是否成功响应2",
  "excerpt": "null",
  "status": "草稿2",
  "createdAt": null,
  "publishedAt": null
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE 删除文章

DELETE /posts

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|query|string| 否 |ID 编号|

> 返回示例

> 200 Response

```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 数据模型

