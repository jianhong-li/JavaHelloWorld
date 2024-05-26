#!/bin/bash



# POM 工具 - deps 读取函数 - 版本A
# 参数列表
# $1: pom 文件路径
# $2: groupId
# $3: artifactId
function pom_read_deps(){
  local POM_FILE="$1"
  local groupId="$2"
  local artifactId="$3"
  local depText

  depText=$(gsed -n -E "
:start
/<dependencyManagement>/,/<\/dependencyManagement>/! {
    /^\s*<dependencies>/ {
        n;
        :ds
        /<dependency>/  {
            :de
            /<\/dependency>/ {
                /${artifactId}/ {
                    /<dependency>\s*[\n]*\s*<groupId>${groupId}<\/groupId>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>/ {
                        p
                        b done
                    }
                    /<dependency>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>\s*[\n]*\s*<groupId>${groupId}<\/groupId>/ {
                        p
                        b done
                    }
                }
                :do_not_math
                n
                b ds
            }
            N;
            b de
        }
        /^\s*<\/dependencies>/ { b stop }
        n;
        b ds
    }
    :done
}
:stop
"  "$POM_FILE")

  if [[ -z "$depText" ]]; then
      echo "no dep found [${groupId}:${artifactId}]" 1>&2
      return
  fi
  echo "$depText"
}

# POM 工具 - deps 读取函数 - 版本B
# 参数列表
# $1: pom 文件路径
# $2: groupId
# $3: artifactId
function pom_read_deps_v1(){
  local POM_FILE="$1"
  local groupId="$2"
  local artifactId="$3"
  local depText

  depText=$(gsed -n -E "
:start
    /^\s*<dependencies>/ {
        # eecho find dependencies
        n;
        :ds
        /<dependency>/  {
            :de
            /<\/dependency>/ {
                /${artifactId}/ {
                    /<dependency>\s*[\n]*\s*<groupId>${groupId}<\/groupId>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>/ {
                        p
                        b done
                    }
                    /<dependency>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>\s*[\n]*\s*<groupId>${groupId}<\/groupId>/ {
                        p
                        b done
                    }
                }
                :dep_not_match
                n;
                b ds
            }
            N;
            b de
        }
        /<\/dependencies>/ { b done }
        n;
        b ds
    }
    /^\s*<dependencyManagement>/ { :skipDMLoop n; /^\s*<\/dependencyManagement>/! {b skipDMLoop }}
    /^\s*<build>/                { :skipBuild  n; /^\s*<\/build>/!                {b skipBuild  }}
:done
"  "$POM_FILE")

  if [[ -z "$depText" ]]; then
      echo "no dep found [${groupId}:${artifactId}]" 1>&2
      return
  fi
  echo "$depText"
}

# POM 工具 - deps 读取函数 - 版本C
# 参数列表
# $1: pom 文件路径
# $2: groupId
# $3: artifactId
function pom_read_deps_v2(){
  local POM_FILE="$1"
  local groupId="$2"
  local artifactId="$3"
  local depText

  depText=$(gsed -n -E "
:start
/^\s*<project.*/ {
    n
    :deps_search_start
    /^\s*<dependencies>/ {
        n;
        :ds
        /<dependency>/  {
            :de
            /<\/dependency>/ {
                /${artifactId}/ {
                    /<dependency>\s*[\n]*\s*<groupId>${groupId}<\/groupId>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>/ {
                        p
                        b done
                    }
                    /<dependency>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>\s*[\n]*\s*<groupId>${groupId}<\/groupId>/ {
                        p
                        b done
                    }
                }
                :dep_not_match
                n;
                b ds
            }
            N;
            b de
        }
        /<\/dependencies>/ { b done }
        n;
        b ds
    }
    /^\s*<dependencyManagement>/ { :skipDMLoop n; /^\s*<\/dependencyManagement>/! {b skipDMLoop }}
    /^\s*<build>/                { :skipBuild  n; /^\s*<\/build>/!                {b skipBuild  }}
    n
    b deps_search_start
}
:done
"  "$POM_FILE")

  if [[ -z "$depText" ]]; then
      echo "no dep found [${groupId}:${artifactId}]" 1>&2
      return
  fi
  echo "$depText"
}

# POM 工具 - deps-dm 读取函数
# 参数列表
# $1: pom 文件路径
# $2: groupId
# $3: artifactId
function pom_read_deps_dm(){
  local POM_FILE="$1"
  local groupId="$2"
  local artifactId="$3"
  local depText

  depText=$(gsed -n -E "
:start
/^\s*<dependencyManagement>/ {        # 匹配开始标签 dm 标签
    n                                 # 匹配完成后,再读取一行替换原内容.这样可以保持使用首行匹配
    :loop
    /^\s*<\/dependencyManagement>/ { b done } # 对于上面的 dep 开始标签的寻找.也有可能找不到开始标签,而是找到了 deps 的结束标签
    /^\s*<dependencies>/ {            # 读到内部的 dependencies 起始标签
        n                             # 再读取一行, 替换掉原来的开始标签.这样可以保持使用首行匹配
        :ds                           # tag: dependency start
        /^\s*<\/dependencies>/ { b done } # 对于上面的 dep 开始标签的寻找.也有可能找不到开始标签,而是找到了 deps 的结束标签
        # 内部标签search1
        /<dependency>/  {             # 读到了我们想要的 dep 的开始标签: dependency
            N;                        # 再读下一行. 此时需要保持内容在模式空间中,供后面读完整后操作
            :de                       # tag: dependency end
            /<\/dependency>/ {        # 读到我们想要的 结束标签. 内部进行处理.处理完成后回退到开始标签的寻找.
                /${artifactId}/ {     # 处理前确定是不是我们想要的 artifactId,如果不是直接 flush 后回退到新的 ds 查找
                    /<dependency>\s*[\n]*\s*<groupId>${groupId}<\/groupId>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>/ {
                        p
                        b done
                    }
                    /<dependency>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>\s*[\n]*\s*<groupId>${groupId}<\/groupId>/ {
                        p
                        b done
                    }
                }
                :dep_not_match
                n;                    # 清空模式空间,回退到 ds 查找
                b ds
            }
            N;                        # 没有读到完整的 dep节点 (即没有找到 dep 的结束).继续读取
            b de                      # 跳转到 de 结束标签判定
        }
        n;                            # 没读到 开始标签 dep. 清空后读下一行
        b ds                          # 跳转到 ds 标签判定处.
    }
    n;                                # 没读到 开始标签 deps. 清空后读下一行
    b loop                            # 跳转到 deps 标签判定处 (即 loop 标签)
    :done
}"  "$POM_FILE")

  if [[ -z "$depText" ]]; then
      echo "no dep found [${groupId}:${artifactId}]" 1>&2
      return
  fi
  echo "$depText"
}

# POM 工具 - deps-dm 读取函数
# 参数列表
# $1: pom 文件路径
# $2: groupId
# $3: artifactId
function pom_read_deps_dm_v2(){
  local POM_FILE="$1"
  local groupId="$2"
  local artifactId="$3"
  local depText

  depText=$(gsed -n -E "
:start
/^\s*<dependencyManagement>/,/^\s*<\/dependencyManagement>/ {
    # 匹配开始标签 deps 标签 (直接匹配.不递归)
    /^\s*<dependencies>/ {           # 读到第一层的 dependencies 起始标签 (不再作为第二层判定. 上面的 dm 地址范围确保了直接找到的是 dm 内部的)
        n                            # 再读取一行, 替换掉原来的开始标签.这样可以保持使用首行匹配
        :ds                          # tag: dependency start
        /<\/dependencies>/ { =;b done } # 对于上面的 dep 开始标签的寻找.也有可能找不到开始标签,而是找到了 deps 的结束标签
        # 内部标签search1
        /<dependency>/  {             # 读到了我们想要的 dep 的开始标签: dependency
            N;                        # 再读下一行. 此时需要保持内容在模式空间中,供后面读完整后操作
            :de                       # tag: dependency end
            /<\/dependency>/ {        # 读到我们想要的 结束标签. 内部进行处理.处理完成后回退到开始标签的寻找.
                /${artifactId}/ {     # 处理前确定是不是我们想要的 artifactId,如果不是直接 flush 后回退到新的 ds 查找
                    /<dependency>\s*[\n]*\s*<groupId>${groupId}<\/groupId>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>/ {
                        p
                        b done
                    }
                    /<dependency>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>\s*[\n]*\s*<groupId>${groupId}<\/groupId>/ {
                        p
                        b done
                    }
                }
                :dep_not_match
                n;                    # 清空模式空间,回退到 ds 查找
                b ds
            }
            N;                        # 没有读到完整的 dep节点 (即没有找到 dep 的结束).继续读取
            b de                      # 跳转到 de 结束标签判定
        }
        n;                            # 没读到 开始标签 dep. 清空后读下一行
        b ds                          # 跳转到 ds 标签判定处.
    }
    # 不用再递归的遍历 search deps 的开始结点.
    :done
}"  "$POM_FILE")

  if [[ -z "$depText" ]]; then
      echo "no dep found [${groupId}:${artifactId}]" 1>&2
      return
  fi
  echo "$depText"
}

# POM 工具 - deps 移除函数
# 参数列表
# $1: pom 文件路径
# $2: groupId
# $3: artifactId
function pom_remove_deps(){
  local POM_FILE="$1"
  local groupId="$2"
  local artifactId="$3"
  local depText

  depText=$(gsed -i -E "
:start
/<dependencyManagement>/,/<\/dependencyManagement>/! {
    :loop
    /<dependencies>/ {
        n;
        :ds
        /<dependency>/  {
            :de
            /<\/dependency>/ {
                /${artifactId}/! {
                    n;
                    b ds
                }
                /${artifactId}/ {
                    /<dependency>\s*[\n]*\s*<groupId>${groupId}<\/groupId>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>/ {
                        d
                        b done
                    }
                    /<dependency>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>\s*[\n]*\s*<groupId>${groupId}<\/groupId>/ {
                        d
                        b done
                    }
                    n;
                    b ds
                }
            }
            N;
            b de
        }
        /<\s*\/dependencies>/{
            b done
        }
        n;
        b ds
  }
  N;
  b loop
  :done
}"  "$POM_FILE")

}

# POM 工具 - deps-dm 移除函数
# 参数列表
# $1: pom 文件路径
# $2: groupId
# $3: artifactId
function pom_remove_deps_dm(){
  local POM_FILE="$1"
  local groupId="$2"
  local artifactId="$3"
  local depText

  depText=$(gsed -n -E "
:start
/<dependencyManagement>/ {
    :loop
    /<dependencies>/ {
        n;
        :ds
        /<dependency>/  {
            :de
            /<\/dependency>/ {
                /${artifactId}/! {
                    n;
                    b ds
                }
                /${artifactId}/ {
                    /<dependency>\s*[\n]*\s*<groupId>${groupId}<\/groupId>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>/ {
                        p
                        b done
                    }
                    /<dependency>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>\s*[\n]*\s*<groupId>${groupId}<\/groupId>/ {
                        p
                        b done
                    }
                    n;
                    b ds
                }
            }
            N;
            b de
        }
        n;
        b ds
  }
  N;
  b loop
  :done
}"  "$POM_FILE")

  if [[ -z "$depText" ]]; then
      echo "no dep found [${groupId}:${artifactId}]"
      return
  fi

  echo "found dep [${groupId}:${artifactId}] in dependencyManagement, remove it"
  echo "------------------------:${POM_FILE}"
  echo "$depText"
  echo "------------------------"
    gsed -i -E "
:start
/<dependencyManagement>/ {
    :loop
    /<dependencies>/ {
        n;
        :ds
        /<dependency>/  {
            :de
            /<\/dependency>/ {
                /${artifactId}/! {
                    n;
                    b ds
                }
                /${artifactId}/ {
                    /<dependency>\s*[\n]*\s*<groupId>${groupId}<\/groupId>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>/ {
                        /.*/d
                        b done
                    }
                    /<dependency>\s*[\n]*\s*<artifactId>${artifactId}<\/artifactId>\s*[\n]*\s*<groupId>${groupId}<\/groupId>/ {
                        /.*/d
                        b done
                    }
                }
            }
            N;
            b de
        }
        n;
        b ds
  }
  N;
  b loop
  :done
}"  "$POM_FILE"

}

# POM 工具 - 删除整理后的 SHIM 依赖列表
# 参数列表:
# $1 - POM FILE
function fix_pom_dep_shim_del_when_upgrade(){
    gsed -i -E '/spring2xDepFixShimStart/,/spring2xDepFixShimEnd/d' "$1"
}

# POM 工具 - 添加整理后的 SHIM 依赖列表
# 参数列表:
# $1 - POM FILE
function fix_pom_dep_shim_add_when_upgrade(){
  local POM_FILE="$1"
  local ESC_DEP
  local SHIM_DEP=$(cat <<EOF
        <!-- spring2xDepFixShimStart ( 不要直接修改此段代码或者往此段声明添加其它依赖,自动化脚本可能会覆盖您的修改 !!!!)  -->
        <!-- 直接声明依赖是为了保证 lombok 的声明顺序在 mapstruct 之前  -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>\${lombok.version}</version>
            <scope>provided</scope>
        </dependency>

        <!--
            org.mapstruct:mapstruct和org.mapstruct:mapstruct-jdk8 是 mapstruct 项目的两个模块。
            org.mapstruct:mapstruct 是核心模块，提供了对象映射的基本功能。 MapStruct 是一个用于生成类型安全的 bean 映射类的 Java 注解处理器。它主要用于把相似类型的 bean 相互转换，例如 DTO 与 DAO 之间的转换。
            org.mapstruct:mapstruct-jdk8 是一个扩展模块，基于 org.mapstruct:mapstruct，额外提供了与 Java 8 特性相关的一些功能，例如对于Optional类型的处理，以及对于使用Java 8日期和时间 API的支持。
            然而需要注意的是，从 MapStruct 1.3.0.Final 版本开始， org.mapstruct:mapstruct-jdk8模块被废弃，Java 8 的特性被直接集成到了 org.mapstruct:mapstruct 中。
            因此在新版本的 MapStruct 中，只需要依赖 org.mapstruct:mapstruct 就可以了，不需要再单独依赖 org.mapstruct:mapstruct-jdk8。
        -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>\${org.mapstruct.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-jdk8</artifactId>
            <version>\${org.mapstruct.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>\${org.mapstruct.version}</version>
            <scope>provided</scope>
        </dependency>
        <!--  新版本 mapstruct 与 lombok 有兼容问题.需要添加此额外依赖.原因是不添加则在编译时会报错
        报错为：Unknown property xxx  in result type  xxx . Did you mean "null"?
        如：java: Unknown property "count" in result type QuizVO. Did you mean "null"?
        参考: https://mapstruct.org/faq/
        注意: lombok 的依赖应该在此定义之前.否则仍然可能不生效 , 另外一种修复办法是直接配置 compiler插件配置信息.
        参考: https://stackoverflow.com/questions/63034956/mapstruct-no-property-named-packaging-exists-in-source-parameters -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok-mapstruct-binding</artifactId>
            <version>\${lombok.mapstruct.binding.version}</version>
            <scope>provided</scope>
        </dependency>
        <!--  请确保这里引入的是 2.17.8版本. -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- spring2xDepFixShimEnd  -->
EOF
)
    # 做 sed 操作时的场景的转义
    ESC_DEP=$(printf '%s\n' "$SHIM_DEP" | sed 's:[\\/&]:\\&:g;$!s/$/\\/')
    # 先删除可能已经存在的 SHIM
    gsed -i -E '/spring2xDepFixShimStart/,/spring2xDepFixShimEnd/d' "$POM_FILE"
    # 重新添加 POM 依赖 SHIM 片段
    gsed -i -E "
:start
/<dependencyManagement>/,/<\/dependencyManagement>/! {
    :loop
    /^\s*<dependencies>/ {
        s/(.*)/\1\n${ESC_DEP}/g
   }
}"  "$POM_FILE"
}

# POM 工具 - 修复入口
function fix_pom_dep_entry(){
  local POM_FILE="$1"
  local groupId artifactId depStr to_remove_dep_list_in_deps to_remove_dep_list_in_DM

  # 删除已经添加的 SHIM
  fix_pom_dep_shim_del_when_upgrade "$POM_FILE"

  # 待删除的 依赖列表. 部分不一定是真的删除.有的可能是重新整理到 SHIM 依赖列表
  to_remove_dep_list_in_deps=$(cat<<EOF
org.hibernate:hibernate-validator
org.projectlombok:lombok
org.mapstruct:mapstruct
org.mapstruct:mapstruct-jdk8
org.mapstruct:mapstruct-processor
org.projectlombok:lombok-mapstruct-binding
org.mockito:mockito-all
EOF
)
  for depStr in ${to_remove_dep_list_in_deps};do
    groupId="${depStr%%:*}"
    artifactId="${depStr#*:}"
    if [[ -z "$(pom_read_deps "$POM_FILE" "$groupId" "$artifactId")" ]]; then
        echo "SHIM DEPS: ${depStr} pass ..."
        continue;
    fi
    echo "SHIM DEPS: ${depStr}"
    pom_remove_deps "$POM_FILE" "$groupId" "$artifactId"
  done

  # 单纯地想删除一些可能错误的定义.主要是写死了依赖版本号的场景
  to_remove_dep_list_in_DM=$(cat<<EOF
org.mockito:mockito-core
EOF
)

  for depStr in ${to_remove_dep_list_in_DM};do
    groupId="${depStr%%:*}"
    artifactId="${depStr#*:}"
    if [[ -n "$(pom_read_deps_dm  "$POM_FILE" "$groupId" "$artifactId")" ]];then
        echo "SHIM DEPS IN DM: ${depStr}"
        pom_remove_deps_dm "$POM_FILE" "$groupId" "$artifactId"
    fi
  done


  fix_pom_dep_shim_add_when_upgrade "$POM_FILE"
}

fix_pom_dep_remove_old_validator(){
  local POM_FILE="$1"
  gsed -i -E "
:start
/<dependency>/ {
  :loop
  /<\/dependency>/ {
     /<artifactId>hibernate-validator<\/artifactId>/ {
        /exclusion/{
          b done
        }
        s/(.*)//g
     }
     b done
  }
  N;
  b loop
  :done
}"  "$POM_FILE"
}

# $1 POM_FILE
pom_has_dependencyManagement(){
  [ -n "$(gsed -E -n '/<dependencyManagement>/,/<\/dependencyManagement>/=' "$1")"  ] && echo 'yes'
}

pom_find_dependencyManagement(){
  if [[ "yes" == "$(pom_has_dependencyManagement "${1}")" ]]; then
      gsed -E -n '/.*<[\/]*dependencyManagement>/=' "$1"  | gsed -E  ':a;$!N;s/\n/,/g;ta'
      return
  fi
  echo '0,0'
}

# $1 POM_FILE
pom_has_dependencies(){
  if [[ -z "$(pom_has_dependencyManagement "$1")" ]]; then
      [ -n "$(gsed -E -n '/<dependencies>/,/<\/dependencies>/=' "$1")"  ] && echo 'yes'
  else
      [ -n "$(gsed -E -n '/<dependencyManagement>/,/<\/dependencyManagement>/!{/<dependencies>/,/<\/dependencies>/=}' "$1")"  ]  && echo 'yes'
  fi
}

analysis_pom_info(){
  # 分析是否包含 dependencyManagement 结点
  local POM_FILE="$1"
  local dependencyManagementLines
  local dependenciesLines
  echo "$1,has_deps:$(pom_has_dependencies "$1"),has dm:$(pom_has_dependencyManagement "$1")"
  dependencyManagementLines=$(
    gsed -E -n '/<dependencyManagement>/,/<\/dependencyManagement>/=' "$POM_FILE"
  )
  if [[ -z "$dependencyManagementLines" ]]; then
      echo "当前POM 文件不包含 dependencyManagement"
  else
      local start end
      start=$(echo "$dependencyManagementLines" | head -n 1)
      end=$(echo "$dependencyManagementLines" | tail -n 1)
      echo "文件包含 dependencyManagement, from: ${start} -> ${end}"
  fi
  dependenciesLines=$(
    gsed -E -n '/<dependencyManagement>/,/<\/dependencyManagement>/!{/<dependencies>/,/<\/dependencies>/=}' "$POM_FILE"
  )

  if [[ -z "$dependenciesLines" ]]; then
      echo "当前POM 文件不包含 dependencies"
  else
      local start end
      start=$(echo "$dependenciesLines" | head -n 1)
      end=$(echo "$dependenciesLines" | tail -n 1)
      echo "文件包含 dependencies, from: ${start} -> ${end}"
  fi

}

#set -x

#fix_pom_dm_remove "pom.xml" "org.mockito" "mockito-core"

#analysis_pom_info "./leo-homework-server/pom.xml"
#pom_find_dependencyManagement "./leo-homework-server/pom.xml"


#x=`pom_read_deps "./leo-homework-server/pom.xml" "com.yuanli.leo" "leo-framework"`
#if [[ -n "$x" ]]; then
#    echo -e "发现了预定义的 DEPS,统一重新在 SHIM片段中重新定义:\n${x}"
#    pom_remove_deps "./leo-homework-server/pom.xml" "com.yuanli.leo" "leo-framework"
#fi


#pom_read_deps "pom.xml" "org.mockito" "mockito-core"

#pom_read_deps "pom.xml" "com.fenbi" "fenbi-rpc-common"
echo 'start'
pom_read_deps_dm "pom_back.xml" "com.fenbi" "fenbi-rpc-client"
#echo mid
#pom_read_deps_dm "pom_back.xml" "org.mockito" "mockito-core"
echo 'end'
echo 'test pom_read_deps_v1 start.......'
pom_read_deps_v1 "pom_back.xml" "com.fenbi" "fenbi-rpc-common"
echo 'test pom_read_deps_v2 start.......'
pom_read_deps_v2 "pom_back.xml" "com.fenbi" "fenbi-rpc-common"
#pom_remove_deps "pom.xml" "org.mockito" "mockito-core"
#pom_read_deps_dm "pom.xml" "org.mockito" "mockito-core"
#pom_remove_deps_dm "pom.xml" "org.mockito" "mockito-core"
#pom_read_deps "./leo-homework-server/pom.xml" "org.hibernate" "hibernate-validator"
#pom_remove_deps "./leo-homework-server/pom.xml" "org.hibernate" "hibernate-validator"
#fix_pom_dep_entry "./leo-homework-server/pom.xml"
#fix_pom_dep_entry "pom.xml"
