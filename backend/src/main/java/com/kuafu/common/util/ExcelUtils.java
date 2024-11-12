package com.kuafu.common.util;

import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.entity.ChangeManagerInfo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Slf4j
public class ExcelUtils {

    public static List<List<String>> readExcel(MultipartFile file) {
        List<List<String>> result = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            DecimalFormat decimalFormat = new DecimalFormat("#.######"); // 可以根据需要设置格式

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> rowList = new ArrayList<>();
                for (Cell cell : row) {
                    // 根据需要处理单元格数据
                    CellType cellType = cell.getCellType();
                    if (cellType == CellType.STRING) {
                        rowList.add(cell.getStringCellValue());
                    } else if (cellType == CellType.NUMERIC) {
                        String formattedValue = decimalFormat.format(cell.getNumericCellValue());
                        rowList.add(formattedValue);
                    } else {
                        rowList.add(cell.getStringCellValue());
                    }
                }
                result.add(rowList);
            }

            inputStream.close();

        } catch (IOException e) {
        }
        return result;
    }

    public static void export(List<ChangeManager> list, HttpServletResponse response) {
        Workbook wb = new SXSSFWorkbook(500);
        try {
            Sheet sheet = wb.createSheet();
            wb.setSheetName(0, "变更明细");

            Row headRow = sheet.createRow(0);
            createHead(headRow);

            for (int i = 0; i < list.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                createData(dataRow, list.get(i));
            }

            wb.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("导出Excel异常{}", e.getMessage());
        } finally {
            IOUtils.closeQuietly(wb);
        }
    }

    private static void createData(Row headRow, ChangeManager changeManager) {

        Cell headCell0 = headRow.createCell(0);
        if (changeManager.getInfoMap().containsKey("客户ECN编号")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("客户ECN编号");
            headCell0.setCellValue(info.getInfoValue());
        } else {
            headCell0.setCellValue("/");
        }

        Cell headCell1 = headRow.createCell(1);
        headCell1.setCellValue(changeManager.getChangeCustomer());

        Cell headCell2 = headRow.createCell(2);
        if (changeManager.getInfoMap().containsKey("ECR编号")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("ECR编号");
            headCell2.setCellValue(info.getInfoValue());
        } else {
            headCell2.setCellValue("/");
        }

        Cell headCell3 = headRow.createCell(3);
        if (changeManager.getInfoMap().containsKey("ECR批准")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("ECR批准");
            headCell3.setCellValue(info.getInfoValue());
        } else {
            headCell3.setCellValue("/");
        }

        Cell headCell4 = headRow.createCell(4);
        if (changeManager.getInfoMap().containsKey("ECN编号")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("ECN编号");
            headCell4.setCellValue(info.getInfoValue());
        } else {
            headCell4.setCellValue("/");
        }

        Cell headCell5 = headRow.createCell(5);
        headCell5.setCellValue(changeManager.getChangeProjectName());

        Cell headCell6 = headRow.createCell(6);
        headCell6.setCellValue(changeManager.getChangeProjectStage());

        Cell headCell7 = headRow.createCell(7);
        headCell7.setCellValue(changeManager.getPartNumber());

        Cell headCell8 = headRow.createCell(8);
        headCell8.setCellValue(changeManager.getChangeProductName());

        Cell headCell9 = headRow.createCell(9);
        if (changeManager.getInfoMap().containsKey("更改前说明")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("更改前说明");
            headCell9.setCellValue(info.getInfoValue());
        } else {
            headCell9.setCellValue("/");
        }

        Cell headCell10 = headRow.createCell(10);
        if (changeManager.getInfoMap().containsKey("更改后说明")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("更改后说明");
            headCell10.setCellValue(info.getInfoValue());
        } else {
            headCell10.setCellValue("/");
        }

        Cell headCell11 = headRow.createCell(11);
        headCell11.setCellValue(changeManager.getChangePersonName());

        Cell headCell12 = headRow.createCell(12);
        headCell12.setCellValue(DateUtils.dateTime(changeManager.getChangeStartTime()));

        Cell headCell13 = headRow.createCell(13);
        headCell13.setCellValue(changeManager.getChangeEndTime() != null
                ? DateUtils.dateTime(changeManager.getChangeEndTime())
                : "");  // 或使用其他默认值

        Cell headCell14 = headRow.createCell(14);
        Cell headCell15 = headRow.createCell(15);
        if (changeManager.getInfoMap().containsKey("3D图纸")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("3D图纸");
            headCell14.setCellValue("●");
            headCell15.setCellValue(DateUtils.dateTime(info.getCreateTime()));
        } else {
            headCell14.setCellValue("○");
            headCell15.setCellValue("/");
        }

        Cell headCell16 = headRow.createCell(16);
        Cell headCell17 = headRow.createCell(17);
        if (changeManager.getInfoMap().containsKey("2D图纸")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("2D图纸");
            headCell16.setCellValue("●");
            headCell17.setCellValue(DateUtils.dateTime(info.getCreateTime()));
        } else {
            headCell16.setCellValue("○");
            headCell17.setCellValue("/");
        }

        Cell headCell18 = headRow.createCell(18);
        Cell headCell19 = headRow.createCell(19);
        if (changeManager.getInfoMap().containsKey("特殊特性清单")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("特殊特性清单");
            headCell18.setCellValue("●");
            headCell19.setCellValue(DateUtils.dateTime(info.getCreateTime()));
        } else {
            headCell18.setCellValue("○");
            headCell19.setCellValue("/");
        }


        Cell headCell20 = headRow.createCell(20);
        Cell headCell21 = headRow.createCell(21);
        if (changeManager.getInfoMap().containsKey("DFEMA")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("DFEMA");
            headCell20.setCellValue("●");
            headCell21.setCellValue(DateUtils.dateTime(info.getCreateTime()));
        } else {
            headCell20.setCellValue("○");
            headCell21.setCellValue("/");
        }

        Cell headCell22 = headRow.createCell(22);
        Cell headCell23 = headRow.createCell(23);
        if (changeManager.getInfoMap().containsKey("PFC")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("PFC");
            headCell22.setCellValue("●");
            headCell23.setCellValue(DateUtils.dateTime(info.getCreateTime()));
        } else {
            headCell22.setCellValue("○");
            headCell23.setCellValue("/");
        }

        Cell headCell24 = headRow.createCell(24);
        Cell headCell25 = headRow.createCell(25);
        if (changeManager.getInfoMap().containsKey("PFEMA")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("PFEMA");
            headCell24.setCellValue("●");
            headCell25.setCellValue(DateUtils.dateTime(info.getCreateTime()));
        } else {
            headCell24.setCellValue("○");
            headCell25.setCellValue("/");
        }

        Cell headCell26 = headRow.createCell(26);
        Cell headCell27 = headRow.createCell(27);
        if (changeManager.getInfoMap().containsKey("CP")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("CP");
            headCell26.setCellValue("●");
            headCell27.setCellValue(DateUtils.dateTime(info.getCreateTime()));
        } else {
            headCell26.setCellValue("○");
            headCell27.setCellValue("/");
        }

        Cell headCell28 = headRow.createCell(28);
        Cell headCell29 = headRow.createCell(29);
        if (changeManager.getInfoMap().containsKey("作业指导书")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("作业指导书");
            headCell28.setCellValue("●");
            headCell29.setCellValue(DateUtils.dateTime(info.getCreateTime()));
        } else {
            headCell28.setCellValue("○");
            headCell29.setCellValue("/");
        }

        Cell headCell30 = headRow.createCell(30);
        Cell headCell31 = headRow.createCell(31);
        if (changeManager.getInfoMap().containsKey("检验指导书")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("检验指导书");
            headCell30.setCellValue("●");
            headCell31.setCellValue(DateUtils.dateTime(info.getCreateTime()));
        } else {
            headCell30.setCellValue("○");
            headCell31.setCellValue("/");
        }

        Cell headCell32 = headRow.createCell(32);
        if (changeManager.getInfoMap().containsKey("验收报告")) {
            headCell32.setCellValue("●");
        } else {
            headCell32.setCellValue("○");
        }

        Cell headCell33 = headRow.createCell(33);
        if (changeManager.getInfoMap().containsKey("备注")) {
            ChangeManagerInfo info = (ChangeManagerInfo) changeManager.getInfoMap().get("备注");
            headCell33.setCellValue(info.getInfoValue());
        } else {
            headCell33.setCellValue("/");
        }
    }

    private static void createHead(Row headRow) {
        Cell headCell0 = headRow.createCell(0);
        headCell0.setCellValue("ECN编号(客户)");

        Cell headCell1 = headRow.createCell(1);
        headCell1.setCellValue("主机厂（客户）");

        Cell headCell2 = headRow.createCell(2);
        headCell2.setCellValue("ECR编号(内部)");

        Cell headCell3 = headRow.createCell(3);
        headCell3.setCellValue("ECR批准");

        Cell headCell4 = headRow.createCell(4);
        headCell4.setCellValue("ECN编号(内部)");

        Cell headCell5 = headRow.createCell(5);
        headCell5.setCellValue("项目名称");

        Cell headCell6 = headRow.createCell(6);
        headCell6.setCellValue("项目阶段");

        Cell headCell7 = headRow.createCell(7);
        headCell7.setCellValue("零件编号");

        Cell headCell8 = headRow.createCell(8);
        headCell8.setCellValue("产品名称");

        Cell headCell9 = headRow.createCell(9);
        headCell9.setCellValue("变更前");

        Cell headCell10 = headRow.createCell(10);
        headCell10.setCellValue("变更后");

        Cell headCell11 = headRow.createCell(11);
        headCell11.setCellValue("项目负责人");

        Cell headCell12 = headRow.createCell(12);
        headCell12.setCellValue("发行日期");

        Cell headCell13 = headRow.createCell(13);
        headCell13.setCellValue("断点日期");

        Cell headCell14 = headRow.createCell(14);
        headCell14.setCellValue("3D图纸");

        Cell headCell15 = headRow.createCell(15);
        headCell15.setCellValue("关闭时间");

        Cell headCell16 = headRow.createCell(16);
        headCell16.setCellValue("2D图纸");

        Cell headCell17 = headRow.createCell(17);
        headCell17.setCellValue("关闭时间");

        Cell headCell18 = headRow.createCell(18);
        headCell18.setCellValue("特殊特性清单");

        Cell headCell19 = headRow.createCell(19);
        headCell19.setCellValue("关闭时间");

        Cell headCell20 = headRow.createCell(20);
        headCell20.setCellValue("DFEMA");

        Cell headCell21 = headRow.createCell(21);
        headCell21.setCellValue("关闭时间");

        Cell headCell22 = headRow.createCell(22);
        headCell22.setCellValue("PFC(工艺流程图)");

        Cell headCell23 = headRow.createCell(23);
        headCell23.setCellValue("关闭时间");

        Cell headCell24 = headRow.createCell(24);
        headCell24.setCellValue("PFEMA");

        Cell headCell25 = headRow.createCell(25);
        headCell25.setCellValue("关闭时间");

        Cell headCell26 = headRow.createCell(26);
        headCell26.setCellValue("CP(控制计划)");

        Cell headCell27 = headRow.createCell(27);
        headCell27.setCellValue("关闭时间");

        Cell headCell28 = headRow.createCell(28);
        headCell28.setCellValue("作业指导书");

        Cell headCell29 = headRow.createCell(29);
        headCell29.setCellValue("关闭时间");

        Cell headCell30 = headRow.createCell(30);
        headCell30.setCellValue("检验指导书");

        Cell headCell31 = headRow.createCell(31);
        headCell31.setCellValue("关闭时间");

        Cell headCell32 = headRow.createCell(32);
        headCell32.setCellValue("验收单");

        Cell headCell33 = headRow.createCell(33);
        headCell33.setCellValue("备注");
    }
}
