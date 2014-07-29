package com.qubaopen.survey.controller.self


public class Test {

	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<>()
		map.put('C', 7)
		map.put('E', 4)
		map.put('R', 3)
		map.put('I', 7)
		map.put('A', 5)
		map.put('S', 5)


		println map.sort()

		def resultMap = [:]

		map.each { k, v -> // 计算每一个类型的分数
			if (resultMap.get(v)) { // key: 分数, value: 种类
				resultMap.get(v) << k
			} else {
				def typeList = []
				typeList << k
				resultMap.put(v, typeList)
			}
		}

		println resultMap.sort().reverseEach {k , v->
			println "$k" + "   $v"
		}
		def resultName = []

		resultMap.sort().reverseEach { k, v ->// 根据key排序,且逆序
			if (resultName.size < 3) {
				def list = v as List
				resultName += list
			}
		}

		println resultName


		Map<Integer, List<String>> map2 = new HashMap<>()
		map2.put(7, 'C')
		map2.put(4 ,'E')
		map2.put(3, 'R')
		def list = map2.get(7) as List
		list.add('I')
//		map2.put(7, 'I')
		map2.put(2, 'A')
		map2.put(5, 'S')
		println ''
		println resultMap.sort()
		println resultMap.size()

//		def list = []
//		list << ['C':7]
//		list << ['E':4]
//		list << ['R':3]
//		list << ['I':7]
//		list << ['A':2]
//		list << ['S':5]

//		println map.keySet().asList().sort()
//		println map.values().asList().sort()
//		println map.sort()
//			def r = map2.sort()
//			println r
//		println map2.sort().reverseEach {k , v->
//			println "$k" + "$v"
//		}
//		println list.sort()
	}
}
