#!/bin/bash


LAUNCHER_PARAM_GIT_USER(){
  local arg_name;
  local arg_value;

  while [[ $# -gt 0 ]]; do
      arg_name="$1"
      case $arg_name in
        -u|--gituser)
            arg_value="$2"
            shift 2;
            break;
            ;;
         *)
            shift 1
            ;;
      esac
  done
  echo "${arg_value}"
}

LAUNCHER_PARAM_SCRIPT(){
  local arg;
  local script;

  while [[ $# -gt 0 ]]; do
      arg="$1"
      case $arg in
        -s|--script)
            script="$2"
            shift 2;
            break;
            ;;
         *)
            shift 1
            ;;
      esac
  done
  echo "${script}"
}

LAUNCHER_PARAM_ACTION(){
  local arg_name;
  local arg_value;

  while [[ $# -gt 0 ]]; do
      arg_name="$1"
      case $arg_name in
        -a|--action)
            arg_value="$2"
            shift 2;
            break;
            ;;
         *)
            shift 1
            ;;
      esac
  done
  echo "${arg_value}"
}


is_valid_json() {
    if jq -e . >/dev/null 2>&1 <<<"$1"; then
        return 0
    else
        return 1
    fi
}

JSON_PARAM(){
  local arg;
  local json;
    # 解析命令行参数,只读取 -j / --json 参数. 其它的不要
    while [[ $# -gt 0 ]]; do
        arg="$1"

        case $arg in
            -j|--json)
                json="$2"
                shift 2
                break
                ;;
            *)
                shift 1
                ;;
        esac
    done

    is_valid_json "$json"

    if [[ ${?} -eq 0 ]]; then
        echo "$json"
        exit 0
    fi
    echo_warn "未读取到json参数"
    echo "{}"
}

# 读取jsonParam的key ,
# $1 json
# $2 key
JSON_PARAM_STRING()
{
  echo "$1" | jq -r "$2"
}


JSON_PARAM_STRING_TRIM2EMPTY()
{
  local value
  value=$(echo "$1" | jq -r "$2")
  if [[ ${value} == null ]]; then
      echo ""
      exit 0
  fi
  echo "$value"
}


JSON_PARAM_OBJ()
{
  echo "$1" | jq  "$2"
}


# JSON数据构建器. 可以像构建MAP一样构建JSON对象
# 示例:
# JSON_BUILDER key1 val1 key2 '{"key":"value"}'
#
# https://unix.stackexchange.com/questions/686785/unix-shell-quoting-issues-error-in-jq-command
# https://stackoverflow.com/questions/70617932/bash-script-to-add-a-new-key-value-pair-dynamically-in-json
JSON_BUILDER(){

  local json='{}';
  for (( cnt = 0; cnt < $#; cnt=cnt+2 )); do
        #echo "key=${*:cnt+1:1}, value=${*:cnt+2:1}"
        local key="${*:cnt+1:1}"
        local val="${*:cnt+2:1}"


        if [[ "${val}" =~ ^[0-9]+(.[0-9]+){0,1}$ ]] || [[ "$val" == "true" ]] || [[ "$val" =~ ^\{.*\}$ ]]; then
          # 场景: bool值 , 数字 , 对象 时, 直接添加
          json=$(echo "$json" | jq '. +=  { '"\"${key}\""' : '"${val}"'}')
        else
          # value是一个string的情况下,要双引号
          json=$(echo "$json" | jq '. +=  { '"\"${key}\""' : '"\"${val}\""'}')
        fi
  done
  #cat <<< $(jq '.student1 += { "Phone'"${m}"'" : '"${i}"'}' Students.json) > Students.json
  echo "${json}"
}

# REST_API 返回数据格式定义
# 格式示例:
# {
#     "ret": true
# }
# 参数:
# $1 - ret (true|false) 必须
# $2 - errcode          可选
# $3 - errmsg           可选
# $4 - data - jsonObject json对象 可选
# 一个参数: API_RESPONSE_RETURN true
# 两个参数: API_RESPONSE_RETURN true  jsonData
# 两个参数: API_RESPONSE_RETURN false msg
# 两个参数: API_RESPONSE_RETURN false code
API_RESPONSE_RETURN(){

  local ret=true
  local errcode=0
  local errmsg=""
  local data=null



  case $# in
    1)
      ret=${1}
      ;;
    2)
      ret="$1"
      # echo "================"
      # echo "$2" | od -c
      # echo "================"

      # 两个参数:
      # 第二个参数可能是 code / msg / data
      # 左边的参数 $2 必须加双引号
      if [[ "$2" =~ ^[-]{0,1}[0-9]+$ ]]; then
        # errCode
          errcode=$2
      elif [[ "$2" =~ ^\{.*\}$  ]]; then
        # data
          data="$2"
      else
          errmsg="$2"
      fi
      ;;
    ?)
      ret=${1}
      errcode="$2"
      errmsg="$3"
      if [[ "$4" =~ \{.*\} ]]; then
          data="$4"
      fi
      ;;
  esac

  if [ "$ret" = false ] && [ "$errcode" = 0 ]; then
     errcode=-1
  fi

  # shellcheck disable=SC2155
  local REST_API_JSON=$(jq -n \
    --arg ret "${ret}" \
    --argjson errcode "$errcode" \
    --arg errmsg "$errmsg" \
    --argjson data "$data" \
    '
      {
          ret: $ret | test("true"),
          errcode: $errcode ,
          errmsg: $errmsg,
          data: $data
      }
      ')
  echo "$REST_API_JSON"
}

# 工具函数: 把字符串中的以空格分割的字符串重新分割为多行.
_func_tool_line_to_multiline(){
  local splitter=' '
  if [ -n "$2" ]; then
      splitter="$2"
  fi
  local multi_lines="${1//${splitter}/$'\n'}"
  echo   "${multi_lines}"
}

_func_tool_multiline_to_line(){
  local single_line="${1//$'\n'/ }"
  echo  "${single_line}"
}