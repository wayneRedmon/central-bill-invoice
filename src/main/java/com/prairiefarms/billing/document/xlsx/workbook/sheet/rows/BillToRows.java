package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.CentralBillAddressCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.CentralBillNameCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.CentralBillPhoneNumberCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.CentralBillStreetCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class BillToRows {

    public static void set(XSSFSheet sheet, CentralBill centralBill) {
        CentralBillNameCell.set(sheet, centralBill.getContact().getName());
        CentralBillStreetCell.set(sheet, centralBill.getContact().getStreet());
        CentralBillAddressCell.set(sheet, centralBill.getContact().getAddress());
        CentralBillPhoneNumberCell.set(sheet, centralBill.getContact().getPhone());
    }
}
