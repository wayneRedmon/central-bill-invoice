package com.prairiefarms;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;

public class XFi_Invoice_PDF implements XFi_Invoice {

	// private static final int maxSummaryLines = 21;
	private static final int maxSummaryLines = 22;

	private static final int maxItemLines = 17;

	private static final Font helveticaNeue9 = FontFactory.getFont(
			"helvetica neue", 9);
	private static final Font helveticaNeue9Italic = FontFactory.getFont(
			"helvetica neue", 9, Font.ITALIC);
	private static final Font helveticaNeue10 = FontFactory.getFont(
			"helvetica neue", 10);
	private static final Font helveticaNeue10Bold = FontFactory.getFont(
			"helvetica neue", 10, Font.BOLD);
	private static final Font helveticaNeue12 = FontFactory.getFont(
			"helvetica neue", 12);
	private static final Font helveticaNeue12Bold = FontFactory.getFont(
			"helvetica neue", 12, Font.BOLD);
	private static final Font helveticaNeue12BoldItalic = FontFactory.getFont(
			"helvetica neue", 12, Font.BOLDITALIC);

	private static final Font courierNew08 = FontFactory.getFont("courier", 8);
	private static final Font courierNew10 = FontFactory.getFont("courier", 10);
	private static final Font courierNew10Red = FontFactory.getFont("courier",
			10, BaseColor.RED);
	private static final Font courierNew10Bold = FontFactory.getFont("courier",
			10, Font.BOLD);
	private static final Font courierNew10BoldRed = FontFactory.getFont(
			"courier", 10, Font.BOLD, BaseColor.RED);
	private static final Font courierNew12Bold = FontFactory.getFont("courier",
			12, Font.BOLD);
	private static final Font courierNew12BoldRed = FontFactory.getFont(
			"courier", 12, Font.BOLD, BaseColor.RED);

	private static final float[] remitWidth = { 1f, 3f, 1f, 1f };
	private static final float[] customerWidth = { 2f, .5f, 2f };
	private static final float[] salesWidth = { 1.5f, 2f, 1f, 2f };
	private static final float[] itemWidth = { .75f, 2.25f, 1f, 1f, 1f };
	private static final float[] totalWidth = { 3f, 1f, 1f, 1f };
	private static final float[] thanksWidth = { 1f, 2f };

	private static final BaseColor background = new BaseColor(197, 220, 236);

	private static final DecimalFormat quantityFormat = new DecimalFormat(
			"#,###;(#,###)");
	private static final DecimalFormat decimalFormat = new DecimalFormat(
			"#,###.00;(#,###.00)");
	private static final DecimalFormat discountFormat = new DecimalFormat(
			"###.000");
	private static final DecimalFormat taxRateFormat = new DecimalFormat(
			"###.0000");

	private static final NumberFormat currencyFormat = NumberFormat
			.getCurrencyInstance();

	private Location location;

	private static ArrayList<Invoice> invoiceList;
	private static Invoice invoice;

	private static double summaryTotal;

	private static ArrayList<Item> itemList;
	private static Item item;

	private static Document document;
	private static Phrase phrase;

	private static Image image;

	private static PdfPTable table;
	private static PdfPCell cell;

	private static boolean printSummary;

	private static int itemCounter;
	private static int page;
	private static int pages;

	private ArrayList<Item> items;

	private	HashSet<Integer> uniqueCustomer;

	private int customer;
	private double customerTotal;


	public void open(String title) {
		try {
			document = new Document();

			PdfWriter.getInstance(document, new FileOutputStream(title.trim()));

			document.addTitle(location.getFrequency() + " Invoices");
			document.addAuthor("Priaire Farms Dairy, Inc.");
			document.addSubject("Invoice(s) for your business.");
			document.addKeywords("PDF, Invoice");
			document.addCreator("Wayne Redmon, Corporate IT, Prairie Farms Dairy, Inc., (606) 875-5801");

			document.open();

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

		invoiceList = new ArrayList<Invoice>();
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void addInvoice(Invoice invoice, String frequency) {
		invoiceList.add(invoice);
	}

	public void setItems() {
		itemCounter = 0;

		for (int pointer = 0; pointer < itemList.size(); pointer++) {
			item = itemList.get(pointer);

			if (invoice.getItem().get(pointer) != null) {
				if (itemCounter > (maxItemLines - 1)) {
					document.newPage();

					itemCounter = 0;

					setBlock_Remit();
					setBlock_Customer();
					setBlock_Sales();
					setBlock_Item();
				}

				try {
					table = new PdfPTable(5);
					table.setWidthPercentage(100);
					table.setWidths(itemWidth);

					phrase = new Phrase();
					if (item.getSalesType().trim().equals("A")) {
						phrase.add(new Chunk(Integer.toString(item.getID())
								+ "  ", courierNew10));
					} else {
						String ledgerNumber = String.format("%07d",
								item.getID());
						phrase.add(new Chunk(ledgerNumber.substring(0, 2) + "-"
								+ ledgerNumber.substring(2, 5) + "-"
								+ ledgerNumber.substring(5, 7), courierNew10));
					}
					cell = new PdfPCell(phrase);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					phrase = new Phrase();
					phrase.add(new Chunk(item.getDescription(), courierNew10));
					cell = new PdfPCell(phrase);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);

					phrase = new Phrase();
					if (item.getQuantity() < 0) {
						phrase.add(new Chunk(quantityFormat.format(item
								.getQuantity()), courierNew10Red));
					} else {
						phrase.add(new Chunk(quantityFormat.format(item
								.getQuantity()), courierNew10));
					}
					cell = new PdfPCell(phrase);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					phrase = new Phrase();
					if (!item.getPromotion().trim().equals("")) {
						phrase.add(new Chunk("(" + item.getPromotion().trim()
								+ ")    "
								+ String.format("%1$,.4f", item.getPrice()),
								courierNew10));
					} else {
						phrase.add(new Chunk(String.format("%1$,.4f",
								item.getPrice()), courierNew10));
					}
					cell = new PdfPCell(phrase);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					item.setExtension(item.getExtension() + 0.0);
					phrase = new Phrase();
					if (item.getExtension() < 0) {
						phrase.add(new Chunk(decimalFormat.format(item
								.getExtension()), courierNew10Red));
					} else {
						phrase.add(new Chunk(decimalFormat.format(item
								.getExtension()), courierNew10));
					}
					cell = new PdfPCell(phrase);
					cell.setBackgroundColor(background);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					document.add(table);

					itemCounter++;

				} catch (DocumentException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setProductSummary(ArrayList<Item> items) {
		this.items = items;
	}

	public void setLogo() {
		try {
			image = Image.getInstance(location.getLogo());

			float scaler = ((document.getPageSize().getWidth()
					- document.leftMargin() - document.rightMargin()) / image
					.getWidth()) * 25;

			image.scalePercent(scaler);

		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setBlock_Summary() {
		itemCounter = 0;

		printSummary = true;

		document.newPage();

		summaryTotal = 0;

		page = 0;

		uniqueCustomer = new HashSet<Integer>();
		for (int i = 0; i < invoiceList.size(); i++) {
			uniqueCustomer.add(invoiceList.get(i).getCustomer().getID());
		}

		if (uniqueCustomer.size() > 1) {
			pages = ((invoiceList.size() + uniqueCustomer.size()) + (maxSummaryLines + 1) - 1) / (maxSummaryLines + 1);

		} else {
			pages = (invoiceList.size() + (maxSummaryLines + 1) - 1) / (maxSummaryLines + 1);
		}

		customer = invoiceList.get(0).getCustomer().getID();
		invoiceList.get(0).getCustomer().getName();
		customerTotal = 0d;

		for (int pointer = 0; pointer < invoiceList.size(); pointer++) {
			invoice = invoiceList.get(pointer);

			if (pointer == 0 && invoice != null) {
				setLogo();
				setBlock_Remit();
				setBlock_Customer();
				setBlock_SummaryHeader();
			}

			invoice.setDiscount((invoice.getSubTotal() - invoice.getApplied())
					* invoice.getBilling().getDiscountRateFixed());

			invoice.setTax((invoice.getSubTotal() - invoice.getApplied() - invoice
					.getBilling().getDiscountRateFixed())
					* invoice.getCustomer().getTaxRateFixed());

			// Commented following line to NOT print TAX
			// invoice.setTotal(invoice.getSubTotal() - invoice.getApplied() -
			// invoice.getDiscount() + invoice.getTax());

			invoice.setTotal(invoice.getSubTotal() - invoice.getApplied()
					- invoice.getDiscount());

			summaryTotal += invoice.getTotal();

			if (invoice != null) {
				if (itemCounter >= maxSummaryLines) {
					document.newPage();

					itemCounter = 0;

					setLogo();
					setBlock_Remit();
					setBlock_Customer();
					setBlock_SummaryHeader();
				}

				try {
					if (uniqueCustomer.size() > 1 && invoice.getCustomer().getID() != customer) {
						table = new PdfPTable(4);
						table.setWidthPercentage(100);
						table.setWidths(totalWidth);

						phrase = new Phrase();
						phrase.add(new Chunk("         CUSTOMER [" + customer
								+ "] TOTAL", courierNew10Bold));
						cell = new PdfPCell(phrase);
						cell.setMinimumHeight(18f);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);

						phrase = new Phrase();
						phrase.add(new Chunk(" ", courierNew10));
						cell = new PdfPCell(phrase);
						cell.setMinimumHeight(18f);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);

						phrase = new Phrase();
						phrase.add(new Chunk(" ", courierNew10));
						cell = new PdfPCell(phrase);
						cell.setMinimumHeight(18f);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);

						phrase = new Phrase();
						if (customerTotal < 0) {
							phrase.add(new Chunk(decimalFormat
									.format(customerTotal), courierNew10BoldRed));
						} else {
							phrase.add(new Chunk(decimalFormat
									.format(customerTotal), courierNew10Bold));
						}
						cell = new PdfPCell(phrase);
						cell.setBackgroundColor(background);
						cell.setMinimumHeight(18f);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);

						document.add(table);

						itemCounter++;

						customer = invoice.getCustomer().getID();
						invoice.getCustomer().getName();
						customerTotal = 0;
					}

					table = new PdfPTable(4);
					table.setWidthPercentage(100);
					table.setWidths(totalWidth);

					phrase = new Phrase();
					phrase.add(new Chunk("[" + invoice.getCustomer().getID()
							+ "]  " + invoice.getCustomer().getName().trim(),
							courierNew10));
					cell = new PdfPCell(phrase);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					table.addCell(cell);

					phrase = new Phrase();

					if (invoice.isUseExtendedID()) {
						phrase.add(new Chunk(invoice.getExtendedID(),
								courierNew08));
					} else {
						phrase.add(new Chunk(Integer.toString(invoice.getID()),
								courierNew10));
					}

					cell = new PdfPCell(phrase);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					phrase = new Phrase();
					phrase.add(new Chunk(invoice.getDeliveryDate_USA(),
							courierNew10));
					cell = new PdfPCell(phrase);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					invoice.setTotal(invoice.getTotal() + 0.0);
					phrase = new Phrase();
					if (invoice.getTotal() < 0) {
						phrase.add(new Chunk(decimalFormat.format(invoice
								.getTotal()), courierNew10Red));
					} else {
						phrase.add(new Chunk(decimalFormat.format(invoice
								.getTotal()), courierNew10));
					}
					cell = new PdfPCell(phrase);
					cell.setBackgroundColor(background);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					if (uniqueCustomer.size() > 1) {
						customerTotal += invoice.getTotal();
					}

					document.add(table);

				} catch (DocumentException e) {
					e.printStackTrace();
				}
			}

			itemCounter++;
		}

		setBlock_SummaryFooter();

		setBlock_ThankYou();

		printSummary = false;
	}

	public void setBlock_SummaryHeader() {
		try {
			table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(totalWidth);

			phrase = new Phrase();
			phrase.add(new Chunk(" "));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" "));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" "));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" "));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("CUSTOMER", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("INVOICE", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("DELIVERED", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("TOTAL", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void setBlock_SummaryFooter() {
		try {
			if (uniqueCustomer.size() > 1) {
				table = new PdfPTable(4);
				table.setWidthPercentage(100);
				table.setWidths(totalWidth);

				phrase = new Phrase();
				phrase.add(new Chunk("         CUSTOMER [" + customer
						+ "] TOTAL", courierNew10Bold));
				cell = new PdfPCell(phrase);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				phrase = new Phrase();
				phrase.add(new Chunk(" ", courierNew10));
				cell = new PdfPCell(phrase);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				phrase = new Phrase();
				phrase.add(new Chunk(" ", courierNew10));
				cell = new PdfPCell(phrase);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				phrase = new Phrase();
				if (customerTotal < 0) {
					phrase.add(new Chunk(decimalFormat
							.format(customerTotal), courierNew10BoldRed));
				} else {
					phrase.add(new Chunk(decimalFormat
							.format(customerTotal), courierNew10Bold));
				}
				cell = new PdfPCell(phrase);
				cell.setBackgroundColor(background);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				document.add(table);

				itemCounter++;
			}

			table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(totalWidth);

			phrase = new Phrase();
			phrase.add(new Chunk(" "));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" "));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("TOTAL", helveticaNeue12Bold));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			summaryTotal += 0.0;
			phrase = new Phrase();
			if (summaryTotal < 0) {
				phrase.add(new Chunk(currencyFormat.format(summaryTotal),
						courierNew10BoldRed));
			} else {
				phrase.add(new Chunk(currencyFormat.format(summaryTotal),
						courierNew10Bold));
			}
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void setBlock_Remit() {
		page++;

		try {
			table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(remitWidth);

			cell = new PdfPCell(image);
			cell.setBorder(0);
			cell.setRowspan(5);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getRemit().getName().trim(),
					helveticaNeue12Bold));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell();
			cell.setBorder(0);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk("INVOICE", helveticaNeue12Bold));
			} else {
				phrase.add(new Chunk("SUMMARY", helveticaNeue12Bold));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getRemit().getAddress1().trim(),
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("Date", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getInvoiceDate_USA(), helveticaNeue12));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getRemit().getAddress_Formatted()
					.trim(), helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk("Number   ", helveticaNeue10));
			} else {
				phrase.add(new Chunk("Page     ", helveticaNeue10));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				if (invoice.isUseExtendedID()) {
					phrase.add(new Chunk(invoice.getExtendedID(),
							helveticaNeue9));

				} else {
					phrase.add(new Chunk(Integer.toString(invoice.getID()),
							helveticaNeue12));
				}

			} else {
				phrase.add(new Chunk(Integer.toString(page) + " of "
						+ Integer.toString(pages), helveticaNeue12));
			}
			cell = new PdfPCell(phrase);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			if (StringUtils.isNotBlank(invoice.getRemit().getPhone_Formatted())) {
				phrase.add(new Chunk("Phone: "
						+ invoice.getRemit().getPhone_Formatted().trim(),
						helveticaNeue10));
			} else {
				phrase.add(new Chunk(""));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk("Customer", helveticaNeue10));
			} else {
				phrase.add(new Chunk("CentralBill", helveticaNeue10));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk(Integer.toString(location.getID()) + "-"
						+ Integer.toString(invoice.getBilling().getID()) + "-"
						+ Integer.toString(invoice.getCustomer().getID()),
						helveticaNeue12));
			} else {
				phrase.add(new Chunk(Integer.toString(invoice.getLocation().getID()) + "-" +
										Integer.toString(invoice.getBilling().getID()),
										helveticaNeue12));
			}

			cell = new PdfPCell(phrase);
			if (!printSummary) {
				cell.setBorderColor(BaseColor.GRAY);
			} else {
				cell.setBorderColor(BaseColor.GRAY);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			if (StringUtils.isNotBlank(invoice.getRemit().getFax_Formatted())) {
				phrase.add(new Chunk("  Fax: "
						+ invoice.getRemit().getFax_Formatted().trim(),
						helveticaNeue10));
			} else {
				phrase.add("");
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk("Page", helveticaNeue10));
			} else {
				phrase.add(new Chunk(" ", helveticaNeue10));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk(Integer.toString(page) + " of "
						+ Integer.toString(pages), helveticaNeue12));
			} else {
				phrase.add(new Chunk(" ", helveticaNeue10));
			}
			cell = new PdfPCell(phrase);
			if (printSummary) {
				cell.setBorder(0);
			} else {
				cell.setBorderColor(BaseColor.GRAY);
			}
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
	}

	public void setBlock_Customer() {
		try {
			table = new PdfPTable(3);
			table.setWidthPercentage(100);
			table.setWidths(customerWidth);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("BILL TO", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk("SHIP TO (if different)",
						helveticaNeue10Bold));
				cell = new PdfPCell(phrase);
				cell.setBorder(0);
				cell.setBorderColor(BaseColor.GRAY);
				cell.setBackgroundColor(background);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			} else {
				phrase.add(new Chunk(" "));
				cell = new PdfPCell(phrase);
				cell.setBorder(0);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getBilling().getName().trim(),
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk(invoice.getCustomer().getName().trim(),
						helveticaNeue10));
			} else {
				phrase.add(new Chunk(" "));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getBilling().getAddress1().trim(),
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk(
						invoice.getCustomer().getAddress1().trim(),
						helveticaNeue10));
			} else {
				phrase.add(new Chunk(" "));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getBilling().getAddress_Formatted()
					.trim(), helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk(invoice.getCustomer()
						.getAddress_Formatted().trim(), helveticaNeue10));
			} else {
				phrase.add(new Chunk(" "));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getBilling().getPhone_Formatted()
					.trim(), helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			if (!printSummary) {
				phrase.add(new Chunk(invoice.getCustomer().getPhone_Formatted()
						.trim(), helveticaNeue10));
			} else {
				phrase.add(new Chunk(" "));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void setBlock_Sales() {
		try {
			table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(salesWidth);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("SALESPERSON", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBackgroundColor(background);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("PURCHASE ORDER", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBackgroundColor(background);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("DELIVERED", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBackgroundColor(background);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("TOTAL DUE BY DATE", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBackgroundColor(background);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getSalesperson().getName(),
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getPurchaseOrder().getContract(),
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getDeliveryDate_USA(), helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();

			if (!invoice.getTerms().getDueByDate_USA().equals(null)) {
				phrase.add(new Chunk(invoice.getTerms().getDueByDate_USA(),
						helveticaNeue12Bold));
			} else {
				phrase.add(new Chunk("*** ERROR ***",
						helveticaNeue12Bold));
			}

			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void setBlock_Item() {
		try {
			table = new PdfPTable(5);
			table.setWidthPercentage(100);
			table.setWidths(itemWidth);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("ITEM", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("DESCRIPTION", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("QUANTITY", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("PRICE EACH", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("TOTAL", helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void setBlock_Total() {
		try {
			table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(totalWidth);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("SUBTOTAL", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			invoice.setSubTotal(invoice.getSubTotal() + 0.0);
			phrase = new Phrase();
			if (invoice.getSubTotal() < 0) {
				phrase.add(new Chunk(currencyFormat.format(invoice
						.getSubTotal()), courierNew10Red));
			} else {
				phrase.add(new Chunk(currencyFormat.format(invoice
						.getSubTotal()), courierNew10));
			}
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("Other Comments or Special Instructions",
					helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorder(0);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("APPLIED", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			invoice.setApplied(invoice.getApplied() * -1);
			invoice.setApplied(invoice.getApplied() + 0.0);
			phrase = new Phrase();
			if (invoice.getApplied() < 0) {
				phrase.add(new Chunk(
						decimalFormat.format(invoice.getApplied()),
						courierNew10Red));
			} else {
				phrase.add(new Chunk(
						decimalFormat.format(invoice.getApplied()),
						courierNew10));
			}
			cell = new PdfPCell(phrase);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();

			if (invoice.getTerms().getDescription() != null) {
				phrase.add(new Chunk("1. Terms are "
						+ invoice.getTerms().getDescription().trim(),
						helveticaNeue10));
			} else {
				phrase.add(new Chunk("1. Terms are - PLEASE CONTACT YOUR A/R REPRESENTATIVE",
						helveticaNeue10));
			}

			cell = new PdfPCell(phrase);
			cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			if (invoice.getBilling().getDiscountRateRaw() != 0) {
				phrase.add(new Chunk("["
						+ discountFormat.format(invoice.getBilling()
								.getDiscountRateRaw()) + "%]",
						helveticaNeue9Italic));
			} else {
				phrase.add(new Chunk(" ", helveticaNeue10));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("DISCOUNTS", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			invoice.setDiscount(invoice.getDiscount() * -1);
			invoice.setDiscount(invoice.getDiscount() + 0.0);
			phrase = new Phrase();
			if (invoice.getDiscount() < 0) {
				phrase.add(new Chunk(
						decimalFormat.format(invoice.getDiscount()),
						courierNew10Red));
			} else {
				phrase.add(new Chunk(
						decimalFormat.format(invoice.getDiscount()),
						courierNew10));
			}
			cell = new PdfPCell(phrase);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(
					"2. Please include the invoice number on your check",
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			if (invoice.getCustomer().getTaxRateRaw() != 0) {
				phrase.add(new Chunk("["
						+ taxRateFormat.format(invoice.getCustomer()
								.getTaxRateRaw()) + "%]", helveticaNeue9Italic));
			} else {
				phrase.add(new Chunk(" ", helveticaNeue10));
			}
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("TAX", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			// invoice.setTax(invoice.getTax() + 0.0);
			phrase = new Phrase();
			phrase.add(new Chunk("0", courierNew10)); // added to NOT print TAX,
														// and commented
														// following 5 lines.
			// if (invoice.getTax() < 0) {
			// phrase.add(new Chunk(decimalFormat.format(invoice.getTax()),
			// courierNew10Red));
			// } else {
			// phrase.add(new Chunk(decimalFormat.format(invoice.getTax()),
			// courierNew10));
			// }
			cell = new PdfPCell(phrase);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("3. Make check(s) payable to:",
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("TOTAL", helveticaNeue12Bold));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			invoice.setTotal(invoice.getTotal() + 0.0);
			phrase = new Phrase();
			if (invoice.getTotal() < 0) {
				phrase.add(new Chunk(currencyFormat.format(invoice.getTotal()),
						courierNew12BoldRed));
			} else {
				phrase.add(new Chunk(currencyFormat.format(invoice.getTotal()),
						courierNew12Bold));
			}
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(location.getDescription().trim(),
					helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("4. Questions? Please contact:",
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("Accounts Receivables at "
					+ invoice.getRemit().getPhone_Formatted().trim() + ".",
					helveticaNeue10Bold));
			cell = new PdfPCell(phrase);
			cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void setBlock_ProductSummary() {
		document.newPage();

		setLogo();

		try {
			table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(remitWidth);

			cell = new PdfPCell(image);
			cell.setBorder(0);
			cell.setRowspan(5);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue12Bold));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("PRODUCT ", helveticaNeue12Bold));
			cell = new PdfPCell();
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("SUMMARY", helveticaNeue12Bold));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getRemit().getAddress1().trim(),
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("Date", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getInvoiceDate_USA(), helveticaNeue12));
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(invoice.getRemit().getAddress_Formatted()
					.trim(), helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("CentralBill", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(Integer.toString(location.getID()) + "-"
								+ Integer.toString(invoice.getBilling().getID()),
								helveticaNeue12));
			cell = new PdfPCell(phrase);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("Phone: "
					+ invoice.getRemit().getPhone_Formatted().trim(),
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" "));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("  Fax: "
					+ invoice.getRemit().getFax_Formatted().trim(),
					helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

		setBlock_Item();

		summaryTotal = 0;

		for (int i = 0; i < items.size(); i++) {
			try {
				item = new Item();

				item = items.get(i);

				summaryTotal += item.getExtension();

				table = new PdfPTable(5);
				table.setWidthPercentage(100);
				table.setWidths(itemWidth);

				phrase = new Phrase();
				phrase.add(new Chunk(Integer.toString(item.getID()) + "  ",
						courierNew10));
				cell = new PdfPCell(phrase);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				phrase = new Phrase();
				phrase.add(new Chunk(item.getDescription(), courierNew10));
				cell = new PdfPCell(phrase);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);

				phrase = new Phrase();
				phrase.add(new Chunk(quantityFormat.format(item.getQuantity()),
						courierNew10));
				cell = new PdfPCell(phrase);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				phrase = new Phrase();
				phrase.add(new Chunk(String.format("%1$,.4f", item.getPrice()),
						courierNew10));
				cell = new PdfPCell(phrase);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				phrase = new Phrase();
				phrase.add(new Chunk(decimalFormat.format(item.getExtension()),
						courierNew10));
				cell = new PdfPCell(phrase);
				cell.setMinimumHeight(18f);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				document.add(table);

			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
		}

		setBlock_ProductSummaryFooter();
	}

	public void setBlock_ProductSummaryFooter() {
		try {
			table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(totalWidth);

			phrase = new Phrase();
			phrase.add(new Chunk(" "));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk(" "));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("TOTAL", helveticaNeue12Bold));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			summaryTotal += 0.0;
			phrase = new Phrase();
			if (summaryTotal < 0) {
				phrase.add(new Chunk(currencyFormat.format(summaryTotal),
						courierNew10BoldRed));
			} else {
				phrase.add(new Chunk(currencyFormat.format(summaryTotal),
						courierNew10Bold));
			}
			cell = new PdfPCell(phrase);
			cell.setBackgroundColor(background);
			cell.setBorderColor(BaseColor.GRAY);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}


	public void setBlock_ThankYou() {
		try {
			table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(thanksWidth);

			if (printSummary) {
				for (int x = 0; x < 2; x++) {
					phrase = new Phrase();
					phrase.add(new Chunk(" ", helveticaNeue10));
					cell = new PdfPCell(phrase);
					cell.setBorder(0);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					phrase = new Phrase();
					phrase.add(new Chunk(" ", helveticaNeue10));
					cell = new PdfPCell(phrase);
					cell.setBorder(0);
					cell.setMinimumHeight(18f);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
				}
			}

			phrase = new Phrase();
			phrase.add(new Chunk(" ", helveticaNeue10));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			phrase = new Phrase();
			phrase.add(new Chunk("Thank You For Your Business!",
					helveticaNeue12BoldItalic));
			cell = new PdfPCell(phrase);
			cell.setBorder(0);
			cell.setMinimumHeight(18f);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			document.add(table);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		setBlock_Summary();

		for (int x = 0; x < invoiceList.size(); x++) {
			invoice = new Invoice();

			invoice = invoiceList.get(x);

			itemList = new ArrayList<Item>(invoice.getItem());

			page = 0;
			pages = 1;

			if (itemList.size() > maxItemLines) {
				pages = (int) itemList.size() / maxItemLines;

				if ((pages < ((double) itemList.size() / maxItemLines))) {
					pages++;
				}
			}

			document.newPage();
			setLogo();
			setBlock_Remit();
			setBlock_Customer();
			setBlock_Sales();
			setBlock_Item();
			setItems();
			setBlock_Total();
			setBlock_ThankYou();
		}

		setBlock_ProductSummary();

		document.close();
	}

}
