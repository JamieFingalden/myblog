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

# Chat

## GET Chat

GET /AIChat/chat

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|prompt|query|string| 否 |none|

> 返回示例

> 200 Response

```
"<think> 好吧，用户又发了一个“helloworld”。看起来他对我很熟悉了。之前我回复过类似的信息，可能他想再次确认我的回应能力或者只是想聊聊天。\n我要考虑他的真实需求是什么。也许他真的需要帮助解决某个问题，但又不想直接提出问题，所以用这样的问候方式来保持对话的开放性。\n如果是这样的话，我可以继续保持友好的态度，并邀请他提出具体的问题或分享他的想法。\n同时，我应该确保回复简洁明了，让用户感到方便继续交流。 </think>\n你好！有什么我可以帮助你的吗？如果你有任何问题或想分享自己的想法，请随时告诉我。"
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 数据模型

<h2 id="tocS_Pet">Pet</h2>

<a id="schemapet"></a>
<a id="schema_Pet"></a>
<a id="tocSpet"></a>
<a id="tocspet"></a>

```json
{
  "id": 1,
  "category": {
    "id": 1,
    "name": "string"
  },
  "name": "doggie",
  "photoUrls": [
    "string"
  ],
  "tags": [
    {
      "id": 1,
      "name": "string"
    }
  ],
  "status": "available"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer(int64)|true|none||宠物ID编号|
|category|[Category](#schemacategory)|true|none||分组|
|name|string|true|none||名称|
|photoUrls|[string]|true|none||照片URL|
|tags|[[Tag](#schematag)]|true|none||标签|
|status|string|true|none||宠物销售状态|

#### 枚举值

|属性|值|
|---|---|
|status|available|
|status|pending|
|status|sold|

<h2 id="tocS_Category">Category</h2>

<a id="schemacategory"></a>
<a id="schema_Category"></a>
<a id="tocScategory"></a>
<a id="tocscategory"></a>

```json
{
  "id": 1,
  "name": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer(int64)|false|none||分组ID编号|
|name|string|false|none||分组名称|

<h2 id="tocS_Tag">Tag</h2>

<a id="schematag"></a>
<a id="schema_Tag"></a>
<a id="tocStag"></a>
<a id="tocstag"></a>

```json
{
  "id": 1,
  "name": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer(int64)|false|none||标签ID编号|
|name|string|false|none||标签名称|

