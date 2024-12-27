package com.ss.price.utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 表格文字识别——使用JSON静态配置文件实现参数外置化
*/
public class Table {
    private static List<String> nameAliases;
    private static List<String> specAliases;

    static {
        try (InputStream inputStream = Table.class.getResourceAsStream("/nameConfig.json");
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            nameAliases = new Gson().fromJson(jsonObject.get("nameAliases"), List.class);
            specAliases = new Gson().fromJson(jsonObject.get("specAliases"), List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * 重要提示代码中所需工具类
    * FileUtil,Base64Util,HttpUtil,GsonUtils请从
    * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
    * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
    * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
    * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
    * 下载
    */
    public static String table() {

        // 调用百度表格OCR_请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/table";

        try {
            // 本地文件路径
            String filePath = "D:\\ssproject\\ssprice\\采购合同\\pic_1\\test\\24-67气缸+接头采购合同双章1891元.jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = Sample.getAccessToken();

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



//  Table.table();
//  假设这是 table() 方法返回的 JSON 字符串
//  String result = table();
    public static void main(String[] args) {

        String result = "{\"tables_result\":[{\"header\":[{\"location\":[{\"x\":589,\"y\":481},{\"x\":2442,\"y\":486},{\"x\":2442,\"y\":564},{\"x\":589,\"y\":559}],\"words\":\"供需双方通过平等协商，本着诚实守信、互惠互利、双方自愿的原则达成一致意见，\"}],\"body\":[{\"col_end\":1,\"row_end\":1,\"cell_location\":[{\"x\":459,\"y\":692},{\"x\":694,\"y\":695},{\"x\":693,\"y\":849},{\"x\":458,\"y\":846}],\"row_start\":0,\"col_start\":0,\"words\":\"序号\"},{\"col_end\":2,\"row_end\":1,\"cell_location\":[{\"x\":694,\"y\":695},{\"x\":1091,\"y\":700},{\"x\":1091,\"y\":855},{\"x\":693,\"y\":849}],\"row_start\":0,\"col_start\":1,\"words\":\"名称\"},{\"col_end\":3,\"row_end\":1,\"cell_location\":[{\"x\":1091,\"y\":700},{\"x\":1594,\"y\":707},{\"x\":1593,\"y\":861},{\"x\":1091,\"y\":855}],\"row_start\":0,\"col_start\":2,\"words\":\"规格参数\"},{\"col_end\":4,\"row_end\":1,\"cell_location\":[{\"x\":1594,\"y\":707},{\"x\":1714,\"y\":709},{\"x\":1712,\"y\":863},{\"x\":1593,\"y\":861}],\"row_start\":0,\"col_start\":3,\"words\":\"单位\"},{\"col_end\":5,\"row_end\":1,\"cell_location\":[{\"x\":1714,\"y\":709},{\"x\":1901,\"y\":711},{\"x\":1900,\"y\":866},{\"x\":1712,\"y\":863}],\"row_start\":0,\"col_start\":4,\"words\":\"数量\"},{\"col_end\":6,\"row_end\":1,\"cell_location\":[{\"x\":1901,\"y\":711},{\"x\":2096,\"y\":714},{\"x\":2094,\"y\":868},{\"x\":1900,\"y\":866}],\"row_start\":0,\"col_start\":5,\"words\":\"单价\\n(元)\"},{\"col_end\":7,\"row_end\":1,\"cell_location\":[{\"x\":2096,\"y\":714},{\"x\":2361,\"y\":717},{\"x\":2359,\"y\":872},{\"x\":2094,\"y\":868}],\"row_start\":0,\"col_start\":6,\"words\":\"金额（元）\"},{\"col_end\":8,\"row_end\":1,\"cell_location\":[{\"x\":2361,\"y\":717},{\"x\":2704,\"y\":722},{\"x\":2703,\"y\":877},{\"x\":2359,\"y\":872}],\"row_start\":0,\"col_start\":7,\"words\":\"备注\"},{\"col_end\":1,\"row_end\":2,\"cell_location\":[{\"x\":458,\"y\":846},{\"x\":693,\"y\":849},{\"x\":693,\"y\":928},{\"x\":458,\"y\":925}],\"row_start\":1,\"col_start\":0,\"words\":\"1\"},{\"col_end\":2,\"row_end\":2,\"cell_location\":[{\"x\":693,\"y\":849},{\"x\":1091,\"y\":855},{\"x\":1090,\"y\":933},{\"x\":693,\"y\":928}],\"row_start\":1,\"col_start\":1,\"words\":\"气缸\"},{\"col_end\":3,\"row_end\":2,\"cell_location\":[{\"x\":1091,\"y\":855},{\"x\":1593,\"y\":861},{\"x\":1592,\"y\":940},{\"x\":1090,\"y\":933}],\"row_start\":1,\"col_start\":2,\"words\":\"MHF2-16D\"},{\"col_end\":4,\"row_end\":2,\"cell_location\":[{\"x\":1593,\"y\":861},{\"x\":1712,\"y\":863},{\"x\":1711,\"y\":942},{\"x\":1592,\"y\":940}],\"row_start\":1,\"col_start\":3,\"words\":\"个\"},{\"col_end\":5,\"row_end\":2,\"cell_location\":[{\"x\":1712,\"y\":863},{\"x\":1900,\"y\":866},{\"x\":1900,\"y\":944},{\"x\":1711,\"y\":942}],\"row_start\":1,\"col_start\":4,\"words\":\"5\"},{\"col_end\":6,\"row_end\":2,\"cell_location\":[{\"x\":1900,\"y\":866},{\"x\":2094,\"y\":868},{\"x\":2093,\"y\":947},{\"x\":1900,\"y\":944}],\"row_start\":1,\"col_start\":5,\"words\":\"212\"},{\"col_end\":7,\"row_end\":2,\"cell_location\":[{\"x\":2094,\"y\":868},{\"x\":2359,\"y\":872},{\"x\":2358,\"y\":950},{\"x\":2093,\"y\":947}],\"row_start\":1,\"col_start\":6,\"words\":\"1060\"},{\"col_end\":8,\"row_end\":2,\"cell_location\":[{\"x\":2359,\"y\":872},{\"x\":2703,\"y\":877},{\"x\":2702,\"y\":955},{\"x\":2358,\"y\":950}],\"row_start\":1,\"col_start\":7,\"words\":\"SMC\"},{\"col_end\":1,\"row_end\":3,\"cell_location\":[{\"x\":458,\"y\":925},{\"x\":693,\"y\":928},{\"x\":692,\"y\":1003},{\"x\":457,\"y\":1000}],\"row_start\":2,\"col_start\":0,\"words\":\"2\"},{\"col_end\":2,\"row_end\":3,\"cell_location\":[{\"x\":693,\"y\":928},{\"x\":1090,\"y\":933},{\"x\":1089,\"y\":1009},{\"x\":692,\"y\":1003}],\"row_start\":2,\"col_start\":1,\"words\":\"弯头接头\"},{\"col_end\":3,\"row_end\":3,\"cell_location\":[{\"x\":1090,\"y\":933},{\"x\":1592,\"y\":940},{\"x\":1591,\"y\":1016},{\"x\":1089,\"y\":1009}],\"row_start\":2,\"col_start\":2,\"words\":\"KQ2L06-M5A\"},{\"col_end\":4,\"row_end\":3,\"cell_location\":[{\"x\":1592,\"y\":940},{\"x\":1711,\"y\":942},{\"x\":1710,\"y\":1018},{\"x\":1591,\"y\":1016}],\"row_start\":2,\"col_start\":3,\"words\":\"个\"},{\"col_end\":5,\"row_end\":3,\"cell_location\":[{\"x\":1711,\"y\":942},{\"x\":1900,\"y\":944},{\"x\":1899,\"y\":1021},{\"x\":1710,\"y\":1018}],\"row_start\":2,\"col_start\":4,\"words\":\"10\"},{\"col_end\":6,\"row_end\":3,\"cell_location\":[{\"x\":1900,\"y\":944},{\"x\":2093,\"y\":947},{\"x\":2091,\"y\":1024},{\"x\":1899,\"y\":1021}],\"row_start\":2,\"col_start\":5,\"words\":\"2.89\"},{\"col_end\":7,\"row_end\":3,\"cell_location\":[{\"x\":2093,\"y\":947},{\"x\":2358,\"y\":950},{\"x\":2357,\"y\":1028},{\"x\":2091,\"y\":1024}],\"row_start\":2,\"col_start\":6,\"words\":\"28.9\"},{\"col_end\":8,\"row_end\":3,\"cell_location\":[{\"x\":2358,\"y\":950},{\"x\":2702,\"y\":955},{\"x\":2702,\"y\":1033},{\"x\":2357,\"y\":1028}],\"row_start\":2,\"col_start\":7,\"words\":\"SMC\"},{\"col_end\":1,\"row_end\":4,\"cell_location\":[{\"x\":457,\"y\":1000},{\"x\":692,\"y\":1003},{\"x\":692,\"y\":1080},{\"x\":457,\"y\":1076}],\"row_start\":3,\"col_start\":0,\"words\":\"3\"},{\"col_end\":2,\"row_end\":4,\"cell_location\":[{\"x\":692,\"y\":1003},{\"x\":1089,\"y\":1009},{\"x\":1089,\"y\":1086},{\"x\":692,\"y\":1080}],\"row_start\":3,\"col_start\":1,\"words\":\"直通调速接头\"},{\"col_end\":3,\"row_end\":4,\"cell_location\":[{\"x\":1089,\"y\":1009},{\"x\":1591,\"y\":1016},{\"x\":1591,\"y\":1093},{\"x\":1089,\"y\":1086}],\"row_start\":3,\"col_start\":2,\"words\":\"AS1002F-06A\"},{\"col_end\":4,\"row_end\":4,\"cell_location\":[{\"x\":1591,\"y\":1016},{\"x\":1710,\"y\":1018},{\"x\":1709,\"y\":1096},{\"x\":1591,\"y\":1093}],\"row_start\":3,\"col_start\":3,\"words\":\"个\"},{\"col_end\":5,\"row_end\":4,\"cell_location\":[{\"x\":1710,\"y\":1018},{\"x\":1899,\"y\":1021},{\"x\":1899,\"y\":1099},{\"x\":1709,\"y\":1096}],\"row_start\":3,\"col_start\":4,\"words\":\"10\"},{\"col_end\":6,\"row_end\":4,\"cell_location\":[{\"x\":1899,\"y\":1021},{\"x\":2091,\"y\":1024},{\"x\":2090,\"y\":1102},{\"x\":1899,\"y\":1099}],\"row_start\":3,\"col_start\":5,\"words\":\"10.85\"},{\"col_end\":7,\"row_end\":4,\"cell_location\":[{\"x\":2091,\"y\":1024},{\"x\":2357,\"y\":1028},{\"x\":2356,\"y\":1106},{\"x\":2090,\"y\":1102}],\"row_start\":3,\"col_start\":6,\"words\":\"108.5\"},{\"col_end\":8,\"row_end\":4,\"cell_location\":[{\"x\":2357,\"y\":1028},{\"x\":2702,\"y\":1033},{\"x\":2701,\"y\":1111},{\"x\":2356,\"y\":1106}],\"row_start\":3,\"col_start\":7,\"words\":\"SMC\"},{\"col_end\":1,\"row_end\":5,\"cell_location\":[{\"x\":457,\"y\":1076},{\"x\":692,\"y\":1080},{\"x\":691,\"y\":1159},{\"x\":457,\"y\":1156}],\"row_start\":4,\"col_start\":0,\"words\":\"4\"},{\"col_end\":2,\"row_end\":5,\"cell_location\":[{\"x\":692,\"y\":1080},{\"x\":1089,\"y\":1086},{\"x\":1089,\"y\":1165},{\"x\":691,\"y\":1159}],\"row_start\":4,\"col_start\":1,\"words\":\"电磁阀\"},{\"col_end\":3,\"row_end\":5,\"cell_location\":[{\"x\":1089,\"y\":1086},{\"x\":1591,\"y\":1093},{\"x\":1590,\"y\":1173},{\"x\":1089,\"y\":1165}],\"row_start\":4,\"col_start\":2,\"words\":\"SY3220-5LZ-C6\"},{\"col_end\":4,\"row_end\":5,\"cell_location\":[{\"x\":1591,\"y\":1093},{\"x\":1709,\"y\":1096},{\"x\":1708,\"y\":1175},{\"x\":1590,\"y\":1173}],\"row_start\":4,\"col_start\":3,\"words\":\"个\"},{\"col_end\":5,\"row_end\":5,\"cell_location\":[{\"x\":1709,\"y\":1096},{\"x\":1899,\"y\":1099},{\"x\":1898,\"y\":1179},{\"x\":1708,\"y\":1175}],\"row_start\":4,\"col_start\":4,\"words\":\"5\"},{\"col_end\":6,\"row_end\":5,\"cell_location\":[{\"x\":1899,\"y\":1099},{\"x\":2090,\"y\":1102},{\"x\":2089,\"y\":1182},{\"x\":1898,\"y\":1179}],\"row_start\":4,\"col_start\":5,\"words\":\"130\"},{\"col_end\":7,\"row_end\":5,\"cell_location\":[{\"x\":2090,\"y\":1102},{\"x\":2356,\"y\":1106},{\"x\":2355,\"y\":1186},{\"x\":2089,\"y\":1182}],\"row_start\":4,\"col_start\":6,\"words\":\"650\"},{\"col_end\":8,\"row_end\":5,\"cell_location\":[{\"x\":2356,\"y\":1106},{\"x\":2701,\"y\":1111},{\"x\":2700,\"y\":1191},{\"x\":2355,\"y\":1186}],\"row_start\":4,\"col_start\":7,\"words\":\"SMC\"},{\"col_end\":1,\"row_end\":6,\"cell_location\":[{\"x\":457,\"y\":1156},{\"x\":691,\"y\":1159},{\"x\":691,\"y\":1240},{\"x\":456,\"y\":1236}],\"row_start\":5,\"col_start\":0,\"words\":\"5\"},{\"col_end\":2,\"row_end\":6,\"cell_location\":[{\"x\":691,\"y\":1159},{\"x\":1089,\"y\":1165},{\"x\":1088,\"y\":1246},{\"x\":691,\"y\":1240}],\"row_start\":5,\"col_start\":1,\"words\":\"汇流板\"},{\"col_end\":3,\"row_end\":6,\"cell_location\":[{\"x\":1089,\"y\":1165},{\"x\":1590,\"y\":1173},{\"x\":1589,\"y\":1253},{\"x\":1088,\"y\":1246}],\"row_start\":5,\"col_start\":2,\"words\":\"SS5Y3-20-05\"},{\"col_end\":4,\"row_end\":6,\"cell_location\":[{\"x\":1590,\"y\":1173},{\"x\":1708,\"y\":1175},{\"x\":1707,\"y\":1256},{\"x\":1589,\"y\":1253}],\"row_start\":5,\"col_start\":3,\"words\":\"个\"},{\"col_end\":5,\"row_end\":6,\"cell_location\":[{\"x\":1708,\"y\":1175},{\"x\":1898,\"y\":1179},{\"x\":1898,\"y\":1259},{\"x\":1707,\"y\":1256}],\"row_start\":5,\"col_start\":4,\"words\":\"1\"},{\"col_end\":6,\"row_end\":6,\"cell_location\":[{\"x\":1898,\"y\":1179},{\"x\":2089,\"y\":1182},{\"x\":2087,\"y\":1262},{\"x\":1898,\"y\":1259}],\"row_start\":5,\"col_start\":5,\"words\":\"31.6\"},{\"col_end\":7,\"row_end\":6,\"cell_location\":[{\"x\":2089,\"y\":1182},{\"x\":2355,\"y\":1186},{\"x\":2354,\"y\":1266},{\"x\":2087,\"y\":1262}],\"row_start\":5,\"col_start\":6,\"words\":\"31.6\"},{\"col_end\":8,\"row_end\":6,\"cell_location\":[{\"x\":2355,\"y\":1186},{\"x\":2700,\"y\":1191},{\"x\":2700,\"y\":1271},{\"x\":2354,\"y\":1266}],\"row_start\":5,\"col_start\":7,\"words\":\"SMC\"},{\"col_end\":1,\"row_end\":7,\"cell_location\":[{\"x\":456,\"y\":1236},{\"x\":691,\"y\":1240},{\"x\":691,\"y\":1320},{\"x\":456,\"y\":1317}],\"row_start\":6,\"col_start\":0,\"words\":\"6\"},{\"col_end\":2,\"row_end\":7,\"cell_location\":[{\"x\":691,\"y\":1240},{\"x\":1088,\"y\":1246},{\"x\":1087,\"y\":1326},{\"x\":691,\"y\":1320}],\"row_start\":6,\"col_start\":1,\"words\":\"消声器\"},{\"col_end\":3,\"row_end\":7,\"cell_location\":[{\"x\":1088,\"y\":1246},{\"x\":1589,\"y\":1253},{\"x\":1588,\"y\":1333},{\"x\":1087,\"y\":1326}],\"row_start\":6,\"col_start\":2,\"words\":\"AN10-01\"},{\"col_end\":4,\"row_end\":7,\"cell_location\":[{\"x\":1589,\"y\":1253},{\"x\":1707,\"y\":1256},{\"x\":1706,\"y\":1336},{\"x\":1588,\"y\":1333}],\"row_start\":6,\"col_start\":3,\"words\":\"个\"},{\"col_end\":5,\"row_end\":7,\"cell_location\":[{\"x\":1707,\"y\":1256},{\"x\":1898,\"y\":1259},{\"x\":1897,\"y\":1339},{\"x\":1706,\"y\":1336}],\"row_start\":6,\"col_start\":4,\"words\":\"2\"},{\"col_end\":6,\"row_end\":7,\"cell_location\":[{\"x\":1898,\"y\":1259},{\"x\":2087,\"y\":1262},{\"x\":2086,\"y\":1342},{\"x\":1897,\"y\":1339}],\"row_start\":6,\"col_start\":5,\"words\":\"4.75\"},{\"col_end\":7,\"row_end\":7,\"cell_location\":[{\"x\":2087,\"y\":1262},{\"x\":2354,\"y\":1266},{\"x\":2353,\"y\":1346},{\"x\":2086,\"y\":1342}],\"row_start\":6,\"col_start\":6,\"words\":\"9.5\"},{\"col_end\":8,\"row_end\":7,\"cell_location\":[{\"x\":2354,\"y\":1266},{\"x\":2700,\"y\":1271},{\"x\":2699,\"y\":1351},{\"x\":2353,\"y\":1346}],\"row_start\":6,\"col_start\":7,\"words\":\"SMC\"},{\"col_end\":1,\"row_end\":8,\"cell_location\":[{\"x\":456,\"y\":1317},{\"x\":691,\"y\":1320},{\"x\":690,\"y\":1397},{\"x\":455,\"y\":1393}],\"row_start\":7,\"col_start\":0,\"words\":\"7\"},{\"col_end\":2,\"row_end\":8,\"cell_location\":[{\"x\":691,\"y\":1320},{\"x\":1087,\"y\":1326},{\"x\":1087,\"y\":1403},{\"x\":690,\"y\":1397}],\"row_start\":7,\"col_start\":1,\"words\":\"螺纹直接接头\"},{\"col_end\":3,\"row_end\":8,\"cell_location\":[{\"x\":1087,\"y\":1326},{\"x\":1588,\"y\":1333},{\"x\":1587,\"y\":1410},{\"x\":1087,\"y\":1403}],\"row_start\":7,\"col_start\":2,\"words\":\"KQ2H08-01AS\"},{\"col_end\":4,\"row_end\":8,\"cell_location\":[{\"x\":1588,\"y\":1333},{\"x\":1706,\"y\":1336},{\"x\":1705,\"y\":1412},{\"x\":1587,\"y\":1410}],\"row_start\":7,\"col_start\":3,\"words\":\"个\"},{\"col_end\":5,\"row_end\":8,\"cell_location\":[{\"x\":1706,\"y\":1336},{\"x\":1897,\"y\":1339},{\"x\":1897,\"y\":1416},{\"x\":1705,\"y\":1412}],\"row_start\":7,\"col_start\":4,\"words\":\"1\"},{\"col_end\":6,\"row_end\":8,\"cell_location\":[{\"x\":1897,\"y\":1339},{\"x\":2086,\"y\":1342},{\"x\":2085,\"y\":1419},{\"x\":1897,\"y\":1416}],\"row_start\":7,\"col_start\":5,\"words\":\"2.92\"},{\"col_end\":7,\"row_end\":8,\"cell_location\":[{\"x\":2086,\"y\":1342},{\"x\":2353,\"y\":1346},{\"x\":2352,\"y\":1423},{\"x\":2085,\"y\":1419}],\"row_start\":7,\"col_start\":6,\"words\":\"2.92\"},{\"col_end\":8,\"row_end\":8,\"cell_location\":[{\"x\":2353,\"y\":1346},{\"x\":2699,\"y\":1351},{\"x\":2698,\"y\":1428},{\"x\":2352,\"y\":1423}],\"row_start\":7,\"col_start\":7,\"words\":\"SMC\"},{\"col_end\":1,\"row_end\":9,\"cell_location\":[{\"x\":455,\"y\":1393},{\"x\":690,\"y\":1397},{\"x\":690,\"y\":1477},{\"x\":455,\"y\":1473}],\"row_start\":8,\"col_start\":0,\"words\":\"备注\"},{\"col_end\":4,\"row_end\":9,\"cell_location\":[{\"x\":690,\"y\":1397},{\"x\":1705,\"y\":1411},{\"x\":1703,\"y\":1491},{\"x\":690,\"y\":1477}],\"row_start\":8,\"col_start\":1,\"words\":\"\"},{\"col_end\":6,\"row_end\":9,\"cell_location\":[{\"x\":1705,\"y\":1411},{\"x\":2084,\"y\":1418},{\"x\":2084,\"y\":1498},{\"x\":1703,\"y\":1491}],\"row_start\":8,\"col_start\":4,\"words\":\"总金额\"},{\"col_end\":7,\"row_end\":9,\"cell_location\":[{\"x\":2085,\"y\":1419},{\"x\":2352,\"y\":1423},{\"x\":2351,\"y\":1502},{\"x\":2084,\"y\":1498}],\"row_start\":8,\"col_start\":6,\"words\":\"1891\"},{\"col_end\":8,\"row_end\":9,\"cell_location\":[{\"x\":2352,\"y\":1423},{\"x\":2698,\"y\":1428},{\"x\":2698,\"y\":1507},{\"x\":2351,\"y\":1502}],\"row_start\":8,\"col_start\":7,\"words\":\"\"},{\"col_end\":8,\"row_end\":10,\"cell_location\":[{\"x\":455,\"y\":1473},{\"x\":2698,\"y\":1507},{\"x\":2697,\"y\":1586},{\"x\":455,\"y\":1551}],\"row_start\":9,\"col_start\":0,\"words\":\"合计人民币大写（√13%增值税；□3%增值税；口不含税）：壹仟捌佰玖拾壹元整\"}],\"table_location\":[{\"x\":455,\"y\":692},{\"x\":2704,\"y\":692},{\"x\":2704,\"y\":1586},{\"x\":455,\"y\":1586}],\"footer\":[{\"location\":[{\"x\":478,\"y\":622},{\"x\":1405,\"y\":638},{\"x\":1404,\"y\":710},{\"x\":477,\"y\":695}],\"words\":\"一、货物名称/规格/数量/价格/供货周期\"},{\"location\":[{\"x\":466,\"y\":1629},{\"x\":2400,\"y\":1665},{\"x\":2399,\"y\":1740},{\"x\":465,\"y\":1704}],\"words\":\"二、货款支付方式：√款到1-3天左右发货；需增值税发票的对公转帐后开增值税发票。\"},{\"location\":[{\"x\":1354,\"y\":1837},{\"x\":2289,\"y\":1854},{\"x\":2288,\"y\":1922},{\"x\":1353,\"y\":1905}],\"words\":\"后由需方自提，自提产生费用由需方自理。\"}]}],\"table_num\":1,\"log_id\":1871401784212131365}\n";
        // 使用 Gson 解析 JSON 字符串
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
        JsonArray tablesResult = jsonObject.getAsJsonArray("tables_result");
        JsonObject firstTable = tablesResult.get(0).getAsJsonObject();
        JsonArray body = firstTable.getAsJsonArray("body");
        // 获取表头信息
        JsonArray headerCells = new JsonArray();
        int firstRowColumnCount = 0;
        for (int i = 0; i < body.size(); i++) {
            JsonObject row = body.get(i).getAsJsonObject();
            int rowStart = row.get("row_start").getAsInt();
            if (rowStart == 0) {
                firstRowColumnCount++;
                headerCells.add(row); // 将表头行添加到 headerCells 中
            } else {
                break; // 假设表头只在第一行，遇到非表头行则停止计数
            }
        }
        System.out.println("表头列数: " + firstRowColumnCount);
        // 创建一个 Map 来存储列名和列索引的映射
        Map<String, Integer> headerIndexMap = new HashMap<>();
        // 遍历表头，找到“名称”和“规格参数”这两列的列索引
        for (int i = 0; i < headerCells.size(); i++) {
            JsonObject cell = headerCells.get(i).getAsJsonObject();
            String words = cell.get("words").getAsString();
            headerIndexMap.put(words, i);
        }
        // 输出表头信息
        System.out.println("表头信息: " + headerCells);

        // 获取“名称”和“规格参数”这两列的列索引
        int nameColumnIndex = -1;
        for (String alias : nameAliases) {
            System.out.println("名称列表中都含有 : " + alias);
            if (headerIndexMap.containsKey(alias)) {
                nameColumnIndex = headerIndexMap.get(alias);
                break;
            }
        }
        if (nameColumnIndex == -1) {
            throw new IllegalArgumentException("没有找到匹配的名称列");
        }
        int specColumnIndex = -1;
        for (String alias : specAliases) {
            System.out.println("型号列表中都含有 : " + alias);
            if (headerIndexMap.containsKey(alias)) {
                specColumnIndex = headerIndexMap.get(alias);
                break;
            }
        }
        if (specColumnIndex == -1) {
            throw new IllegalArgumentException("没有找到匹配的规格参数列");
        }
        // 输出表头信息
        System.out.println("**********************************************************************");
        System.out.println("名称列索引: " + nameColumnIndex);
        System.out.println("规格参数列索引: " + specColumnIndex);

        // 创建一个 Map 来存储结果
        Map<Integer, Map<String, String>> resultMap = new HashMap<>();

        // 遍历 body 中的每一行，从第二行开始
        for (int i = 0; i < body.size(); i++) {
            JsonObject row = body.get(i).getAsJsonObject();
            int rowStart = row.get("row_start").getAsInt();
            if (rowStart == 0) {
                continue;
            }
            int colStart = row.get("col_start").getAsInt();
            String words = row.get("words").getAsString();

            // 将当前行的单元格内容放入 resultMap 中
            resultMap.putIfAbsent(rowStart, new HashMap<>());
            resultMap.get(rowStart).putIfAbsent(String.valueOf(colStart), words);
        }
        // 打印 resultMap 以验证结果
        for (Map.Entry<Integer, Map<String, String>> entry : resultMap.entrySet()) {
            System.out.println("Row " + entry.getKey() + ": " + entry.getValue());
        }
        // 根据列索引获取“名称”和“规格参数”这两列的内容
        for (Map.Entry<Integer, Map<String, String>> entry : resultMap.entrySet()) {
            Integer rowIndex = entry.getKey();
            Map<String, String> rowContent = entry.getValue();
            String name = rowContent.getOrDefault(String.valueOf(nameColumnIndex), "");
            String spec = rowContent.getOrDefault(String.valueOf(specColumnIndex), "");
            if (name.isEmpty() || spec.isEmpty()) {
                continue;
            }
            System.out.println("Row " + rowIndex + ": 名称=" + name + ", 规格参数=" + spec);
        }
    }
}
