package com.ss.price.controller.test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ss.price.entity.ConfigItems;
import com.ss.price.entity.ConfigTypes;
import com.ss.price.entity.dto.RespondDto;
import com.ss.price.mapper.ConfigItemsMapper;
import com.ss.price.mapper.ConfigTypesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/ss/baidu")
@Slf4j

/**
 *  百度OCR——图片文本文字识别测试（使用数据库配置实现参数外置化）
 *
 * http://localhost:20122/ss/baidu/word
 */
public class BaiduWordIdentify {
    @Resource
    ConfigTypesMapper configTypesMapper;
    @Resource
    ConfigItemsMapper configItemsMapper;
    @RequestMapping(value = "/picWord", method = {RequestMethod.GET, RequestMethod.POST})
    public RespondDto getWordIdentify() {
        // 从数据库中读取配置
        List<ConfigTypes> configTypes = configTypesMapper.selectList(null);
        Map<String, List<ConfigItems>> configItemsMap = new HashMap<>();
        for (ConfigTypes configType : configTypes) {
            List<ConfigItems> items = configItemsMapper.selectByMap(Collections.singletonMap("config_type_id", configType.getId()));
            configItemsMap.put(configType.getTypeName(), items);
        }
        List<String> manufacturerAliases = configItemsMap.getOrDefault("厂家", Collections.emptyList()).stream().map(ConfigItems::getAlias).collect(Collectors.toList());
        System.out.println("manufacturerAliases = " + manufacturerAliases);
        List<String> phoneAliases = configItemsMap.getOrDefault("电话", Collections.emptyList()).stream().map(ConfigItems::getAlias).collect(Collectors.toList());
        System.out.println("phoneAliases = " + phoneAliases);
        List<String> contactAliases = configItemsMap.getOrDefault("联系人", Collections.emptyList()).stream().map(ConfigItems::getAlias).collect(Collectors.toList());
        System.out.println("contactAliases = "+ contactAliases);
        List<String> purchaseTimeAliases = configItemsMap.getOrDefault("采购时间", Collections.emptyList()).stream().map(ConfigItems::getAlias).collect(Collectors.toList());
        System.out.println("purchaseTimeAliases = " + purchaseTimeAliases);

        // D:\\ssproject\\ssprice\\采购合同\\pic_1\\test\\24-67气缸+接头采购合同双章1891元.jpg  识别之后的文字版结果
        String result = "{\"words_result\":[{\"words\":\"AUTOMATION\"},{\"words\":\"购销合同\"},{\"words\":\"供方：乐清市埃立客气动科技有限公司\"},{\"words\":\"合同编号：ALC0020240711001\"},{\"words\":\"需方:_陕西晟思智能测控有限公司\"},{\"words\":\"签订时间：2024.07.11\"},{\"words\":\"供需双方通过平等协商，本着诚实守信、互惠互利、双方自愿的原则达成一致意见，\"},{\"words\":\"同意签订本合同，具体条款如下：\"},{\"words\":\"一、货物名称/规格/数量/价格/供货周期\"},{\"words\":\"序号\"},{\"words\":\"名称\"},{\"words\":\"规格参数\"},{\"words\":\"单价\"},{\"words\":\"单位\"},{\"words\":\"数量\"},{\"words\":\"(元)\"},{\"words\":\"金额（元）\"},{\"words\":\"备注\"},{\"words\":\"1\"},{\"words\":\"气缸\"},{\"words\":\"MHF2-16D\"},{\"words\":\"个\"},{\"words\":\"5\"},{\"words\":\"212\"},{\"words\":\"1060\"},{\"words\":\"SMC\"},{\"words\":\"2\"},{\"words\":\"弯头接头\"},{\"words\":\"KQ2L06-M5A\"},{\"words\":\"个\"},{\"words\":\"10\"},{\"words\":\"2.89\"},{\"words\":\"28.9\"},{\"words\":\"SMC\"},{\"words\":\"3\"},{\"words\":\"直通调速接头\"},{\"words\":\"AS1002F-06A\"},{\"words\":\"个\"},{\"words\":\"10\"},{\"words\":\"10.85\"},{\"words\":\"108.5\"},{\"words\":\"SMC\"},{\"words\":\"4\"},{\"words\":\"电磁阀\"},{\"words\":\"SY3220-5LZ-C6\"},{\"words\":\"个\"},{\"words\":\"5\"},{\"words\":\"130\"},{\"words\":\"650\"},{\"words\":\"SMC\"},{\"words\":\"5\"},{\"words\":\"汇流板\"},{\"words\":\"SS5Y3-20-05\"},{\"words\":\"个\"},{\"words\":\"31.6\"},{\"words\":\"31.6\"},{\"words\":\"SMC\"},{\"words\":\"6\"},{\"words\":\"消声器\"},{\"words\":\"AN10-01\"},{\"words\":\"个\"},{\"words\":\"2\"},{\"words\":\"4.75\"},{\"words\":\"9.5\"},{\"words\":\"SMC\"},{\"words\":\"7\"},{\"words\":\"螺纹直接接头\"},{\"words\":\"KQ2H08-01AS\"},{\"words\":\"个\"},{\"words\":\"1\"},{\"words\":\"2.92\"},{\"words\":\"2.92\"},{\"words\":\"SMC\"},{\"words\":\"备注\"},{\"words\":\"总金额\"},{\"words\":\"1891\"},{\"words\":\"合计人民币大写（√13%增值税；□3%增值税；口不含税）：壹仟捌佰玖拾壹元整\"},{\"words\":\"二、货款支付方式：√款到1-3天左右发货；需增值税发票的对公转帐后开增值税发票。\"},{\"words\":\"三、交货地点、方式：\"},{\"words\":\"口物流运输：供方承担费用至\"},{\"words\":\"后由需方自提，自提产生费用由需方自理。\"},{\"words\":\"□到港：供方承担到港前的运输费用和保险费用及其他所有费用，到港前运输过程中的一切损坏、\"},{\"words\":\"错发、漏发等风险均由供方负责。\"},{\"words\":\"√快递送货上门：供方承担运输费用和保险费用及其他所有费用，运输过程中的一切损坏、错发、\"},{\"words\":\"漏发等风险均由供方负责。\"},{\"words\":\"四、验收时间及提出异议期限：货到后7日内根据合同约定和国家现行标准、行业标准验收，如有\"},{\"words\":\"质量问题或供方错发等由供方免费更换，到货7日后视为验收合格。\"},{\"words\":\"五、合同解决争议的办法：双方友好协商解决，若协商不成，可向供方所在地人民法院起诉。\"},{\"words\":\"六、本合同一式两份，供、需双方各执一份，具有同等法律效力\"},{\"words\":\"供方（盖章）：乐清市埃立客气动科技有限公司\"},{\"words\":\"代表（签字）：小陆\"},{\"words\":\"需方（盖章）：陕西晟思智能测控有限公司\"},{\"words\":\"代表（签字）\"},{\"words\":\"联系电话：15057322501（微信同号）\"},{\"words\":\"联系电话：\"},{\"words\":\"日期：2024年07月11日\"},{\"words\":\"日期：2024年07月11日\"},{\"words\":\"帐号及汇款资料\"},{\"words\":\"名称：乐清市埃立客气动科技有限公司\"},{\"words\":\"银行:中国建设银行股份有限公司乐清新虹支行\"},{\"words\":\"税号：91330382MA2CT81242\"},{\"words\":\"帐号：33050162756500000536\"},{\"words\":\"第1页，共1页\"}],\"words_result_num\":103,\"log_id\":\"1875010279962929591\"}" ;// 使用 Gson 解析 JSON 字符串
        // 使用 Gson 解析 JSON 字符串
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
        JsonArray wordsResult = jsonObject.getAsJsonArray("words_result");

        // 创建一个 Map 来存储结果
        Map<String, String> extractedInfo = new HashMap<>();

        // 遍历 words_result 数组
        for (int i = 0; i < wordsResult.size(); i++) {
            JsonObject wordObj = wordsResult.get(i).getAsJsonObject();
            String word = wordObj.get("words").getAsString();

            // 检查是否匹配
            // manufacturerAliases.stream(): 将 manufacturerAliases 列表转换为一个流。
            // anyMatch(word::contains): 检查流中的任何一个元素是否是 word 的子字符串。word::contains 是一个方法引用，
            // 表示对流中的每个元素调用 word.contains(element) 方法。
            if (manufacturerAliases.stream().anyMatch(word::contains)) {
                extractedInfo.put("厂家", word);
            }

            // 检查是否匹配 phoneAliases
            if (phoneAliases.stream().anyMatch(word::contains)) {
                extractedInfo.put("电话", word);
            }

            // 检查是否匹配 contactAliases
            if (contactAliases.stream().anyMatch(word::contains)) {
                extractedInfo.put("联系人", word);
            }

            // 检查是否匹配 purchaseTimeAliases
            if (purchaseTimeAliases.stream().anyMatch(word::contains)) {
                extractedInfo.put("采购时间", word);
            }
        }
        // 打印提取的信息
        System.out.println("提取的信息: " + extractedInfo);
        return new RespondDto(0, "success", null);
    }




    // 定义一个方法来清理字符串
    private String cleanString(String input) {
        if (input == null) {
            return "";
        }
        // 使用正则表达式去除换行符、转义字符以及中英文形式的括号
        return input.replaceAll("[\\n\\r\\\\（）()]", "").trim();
    }
}
