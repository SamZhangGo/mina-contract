# mina-contract

![当前最新mina-contract版本号](https://img.shields.io/badge/minacontract-1.0-green.svg)
![JAVA支持版本号](https://img.shields.io/badge/JAVA-1.8+-green.svg)

用于解析mina接口的字节输入输出，使用者只需在协议用json或者yaml定义在resource下即可
* JSON
```json
{
  "0x0101": [
    [
      {
        "name": "stringParam",
        "size": 100,
        "type": "ByteArray",
        "func": "Normal"
      },
      {
        "name": "longParam",
        "size": 8,
        "type": "Long",
        "func": "Normal"
      },
      {
        "name": "tmpId",
        "size": 4,
        "type": "Int",
        "func": "Normal"
      },
      {
        "name": "port",
        "size": 2,
        "type": "Short",
        "func": "Normal"
      }
    ]
  ]
}
```
* yaml
```yaml
#anchor define zone
anchor:
  - bn: &byteNormal {type: ByteArray, func: Normal}
  - ln: &longNormal {size: 8, type: Long, func: Normal}
  - sn: &shortNormal {size: 2, type: Short, func: Normal}
  - in: &intNormal {size: 4, type: Int, func: Normal}

0x0101:
  - - name: stringParam
      size: 100
      <<: *byteNormal
    - name: longParam
      <<: *longNormal
    - name: intParam
      <<: *intNormal
    - name: shortParam
      <<: *shortNormal
```



