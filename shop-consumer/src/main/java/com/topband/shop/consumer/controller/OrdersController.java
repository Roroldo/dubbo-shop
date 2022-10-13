package com.topband.shop.consumer.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.PageUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.topband.shop.api.OrdersService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.consumer.anno.limit.RateLimiter;
import com.topband.shop.consumer.anno.log.LogEnum;
import com.topband.shop.consumer.anno.log.LogRecordAnno;
import com.topband.shop.dto.OrdersDTO;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.utils.CheckPageUtils;
import com.topband.shop.utils.UserHolder;
import com.topband.shop.view.OrdersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.topband.shop.constants.BusinessConstants.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: OrdersController
 * @packageName: com.topband.shop.consumer.controller
 * @description: OrdersController
 * @date 2022/9/13 17:02
 */
@Slf4j
@Api(tags = "订单接口")
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @DubboReference
    private OrdersService ordersService;

    @PostMapping("/create/{goodsId}")
    @ApiOperation("创建订单")
    public Result createOrders(@PathVariable Long goodsId) {
        if (goodsId == null || goodsId <= 0) {
            return Result.fail();
        }
        // 封装用户 id，商品 id
        Long userId = UserHolder.getUser().getId();
        // 订单流水号，防止 dubbo 重试创建多笔订单
        String ordersSerial = UUID.randomUUID().toString(true);
        OrdersDTO ordersDTO = new OrdersDTO(userId, goodsId, ordersSerial);
        return ordersService.spikeGoods(ordersDTO);
    }

    @PostMapping("/list")
    @ApiOperation("查询订单")
    public Result list(@RequestBody PageQueryDTO pageQueryDTO) {
        return Result.ok(ordersService.list(pageQueryDTO));
    }

    @GetMapping("/export")
    @ApiOperation("导出订单 excel")
    @RateLimiter(count = 1, timeunit = TimeUnit.SECONDS, timeout = 30)
    @LogRecordAnno(LogEnum.EXPORT_ORDERS_EXCEL)
    public void export(@RequestBody PageQueryDTO pageQueryDTO, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(EXPORT_ORDERS_EXCEL_NAME, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        int currentPage = CheckPageUtils.getCurrentPage(pageQueryDTO.getCurrentPage());
        int pageSize = CheckPageUtils.getPageSize(pageQueryDTO.getPageSize());
        // 计算开始位置
        int startIndex = PageUtil.getStart(currentPage - 1, pageSize);
        // 导出记录从当前页，开始导出 10000 条记录
        pageQueryDTO.setStartIndex(startIndex);
        pageQueryDTO.setEndIndex(EXPORT_ORDERS_PAGE_SIZE);

        int count = EXPORT_ORDERS_MAX / EXPORT_ORDERS_PAGE_SIZE;
        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(outputStream, OrdersVO.class).build();
        try {
            for (int i = 1; i <= count; i++) {
                // 查询
                List<OrdersVO> ordersVOList = ordersService.listManual(pageQueryDTO);
                if (ordersVOList.size() == 0) {
                    break;
                }
                WriteSheet writeSheet = EasyExcel.writerSheet(EXPORT_ORDERS_EXCEL_SHEET_NAME)
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
                excelWriter.write(ordersVOList, writeSheet);
                startIndex = startIndex + EXPORT_ORDERS_PAGE_SIZE;
                pageQueryDTO.setStartIndex(startIndex);
            }
            excelWriter.finish();
        } catch (Exception e) {
            handleFileDownloadError(response, e);
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    private void handleFileDownloadError(HttpServletResponse response, Exception e) throws IOException {
        log.error("下载文件失败：{}", ExceptionUtils.getStackTrace(e));
        JSONConfig jsonConfig = JSONConfig.create().setIgnoreNullValue(false);
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(Result.fail(ResultCodeEnum.FILE_DOWNLOAD_ERROR), jsonConfig));
    }
}
