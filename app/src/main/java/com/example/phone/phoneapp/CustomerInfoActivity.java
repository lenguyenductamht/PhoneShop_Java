package com.example.phone.phoneapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.example.phone.model.OrderDetail;
import com.example.phone.model.Orders;
import com.example.phone.model.Products;
import com.example.phone.myadapter.PdfDocumentAdapter;
import com.example.phone.myadapter.ViewOrderDetailAdapter;
import com.example.phone.ultil.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomerInfoActivity extends AppCompatActivity {
    private EditText edtPhonNumber, edtName, edtAddress, edtEmail;
    private Button btnContinue, btnUpdateCustomer, btnOk, btnPrint;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mData;
    private DatabaseReference mReference;
    private StorageReference storageReference;
    static String discountId2;
    static double totalFinal;
    Orders od1= new Orders();
    static String tenNhanVien;
    int sumThanhTien = 0;

    public ViewOrderDetailAdapter viewOrderDetailAdapter;

    Dialog dialog;
    ArrayList<OrderDetail> arrayList;
    Products pro = new Products();
    public static boolean keyReturn = false; // đang k dùng layout cho confirm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        mapping();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Query query = mReference.orderByChild("phoneNumber").equalTo(bundle.getString("phone"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    //get data
                    String ten = ""+ds.child("name").getValue();
                    String dc = ""+ds.child("address").getValue();
                    String e = ""+ds.child("email").getValue();

                    //set data
                    edtName.setText(ten);
                    edtAddress.setText(dc);
                    edtPhonNumber.setText(bundle.getString("phone"));
                    edtEmail.setText(e);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        btnUpdateCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        Dexter.withActivity(CustomerInfoActivity.this)
//                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        btnPrint.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                createPDFFile(Common.getAppPath(CustomerInfoActivity.this)+"test_pdf.pdf");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                })
//                .check();
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pdfFilename = "src/resources/Invoice_Ex.pdf";
                createPDFFile(pdfFilename);
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //Lưu thông tin mua hàng lên firebase
                // Lưu vào bảng Orders
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String orderId = mData.child("Orders").push().getKey(); // lấy kye cua firbase
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                od1= new Orders(userId, orderId, currentDate, discountId2, totalFinal);
                mData.child("Orders").child(orderId).setValue(od1);


                //Lưu vào bảng Order Detail

                for (int i = 0; i < MainActivity.arrayCart.size(); i++){
                    int productId = MainActivity.arrayCart.get(i).getProductId();
                    int price1 = MainActivity.arrayCart.get(i).getPrice();
                    int quantity = MainActivity.arrayCart.get(i).getQuantity();
                    OrderDetail orderDetail = new OrderDetail(orderId, productId,
                            price1, quantity);
                    mData.child("Order Detail").push().setValue(orderDetail);
                    //Cập nhật số lượng sp bảng products
                    mData.child("Products").child(String.valueOf(productId)).child("quantity").setValue(InfoProductsActivity.slConLai - quantity);
                }




                //xóa sp trong giỏ
                btnPrint.performClick();
                MainActivity.arrayCart.clear();

                dialog.setContentView(R.layout.dialog_order);
                dialog.setCanceledOnTouchOutside(false);
                btnOk = dialog.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        dialog.dismiss();
                        CartActivity.discount1= null;
                        InforDiscountActivity.discounts = null;
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

//        Dexter.withActivity(CustomerInfoActivity.this)
//                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        dialog.setContentView(R.layout.dialog_order);
//                        dialog.setCanceledOnTouchOutside(false);
//                        btnCreatePDF = dialog.findViewById(R.id.btn_create_pdf);
//                        btnSendEmail = dialog.findViewById(R.id.btn_send_email);
//                        btnCreatePDF.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                createPDFFile(Common.getAppPath(getApplicationContext())+"test_pdf.pdf");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                })
//                .check();
    }

//    private void createPDFFile(String path) {
//        if(new File(path).exists())
//            new File(path).delete();
//        try{
//            Document document = new Document();
//            //Save
//            PdfWriter.getInstance(document, new FileOutputStream(path));
//            document.open();
//
//            document.setPageSize(PageSize.A4);
//            document.addCreationDate();
//            document.addAuthor("TTShop");
//            document.addCreator("tenNhanVien");
//
//            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
//            float fontSize = 20.0f;
//            float valueFontSize = 26.0f;
//
//            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
//
//            Font titleFont = new Font(fontName, 36, Font.NORMAL, BaseColor.BLACK);
//            addNewItem(document, "Hóa đơn", Element.ALIGN_CENTER, titleFont);
//
//            //Add more
//            Font orderNumberFont = new Font(fontName, fontSize, Font.NORMAL, colorAccent);
//            Font orderNumberValueFont = new Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK);
//
//
//            addNewItemWithLeftAndRight(document, "Mã hóa đơn", od1.getOrderId(), orderNumberFont, orderNumberValueFont);
//            addNewItemWithLeftAndRight(document, "Ngày tạo", od1.getOrderDate(), orderNumberFont, orderNumberValueFont);
//            addNewItemWithLeftAndRight(document, "Nhân viên", tenNhanVien, orderNumberFont, orderNumberValueFont);
//
//
//            //addNewItem(document, "#717171", Element.ALIGN_LEFT, orderNumberValueFont);
//
//            addLineSeperator(document);
//
//            addLineSpace(document);
//            addNewItem(document, "Chi tiết hóa đơn", Element.ALIGN_CENTER, titleFont);
//            addLineSeperator(document);
//            addNewItemFull(document, "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền", orderNumberFont, orderNumberValueFont);
//            addLineSpace(document);
//            mData.child("Order Detail").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot dt : dataSnapshot.getChildren()){
//                        OrderDetail od = dt.getValue(OrderDetail.class);
//                        if(od.getOrderId().equals(od1.getOrderId())){
////                            arrayList.add(od);
////                            viewOrderDetailAdapter.notifyDataSetChanged();
//                            mData.child("Products").orderByChild("productId").addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    for (DataSnapshot dt : dataSnapshot.getChildren()){
//                                        Products products = dt.getValue(Products.class);
//                                        if(String.valueOf(products.getProductId()).equals(od.getProductId())){
//
//                                            pro.setProductName(products.getProductName());
//                                        }
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                    Toast.makeText(getApplicationContext(), "Error:" +
//                                            databaseError.getMessage() , Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            try {
//                                addNewItemFull(document, pro.getProductName(), String.valueOf(od.getPrice()), String.valueOf(od.getQuantity()), String.valueOf(od.getPrice()*od.getQuantity()), orderNumberFont, orderNumberValueFont);
//                                sumThanhTien+=od.getPrice()*od.getQuantity();
//                            } catch (DocumentException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        try {
//                            addLineSeperator(document);
//                        } catch (DocumentException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(getApplicationContext(), "Error:" +
//                            databaseError.getMessage() , Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            addLineSeperator(document);
//
//            addLineSpace(document);
//            addNewItemWithLeftAndRight(document, "Tổng cộng", String.valueOf(sumThanhTien), titleFont, orderNumberValueFont);
//            addNewItemWithLeftAndRight(document, "Giảm giá", String.valueOf(0), titleFont, orderNumberValueFont);
//
//
//            addLineSpace(document);
//            addLineSpace(document);
//            addNewItemWithLeftAndRight(document, "Tiền phải trả", String.valueOf(sumThanhTien), titleFont, orderNumberValueFont);
//
//            document.close();
//            Toast.makeText(this, "Success",Toast.LENGTH_SHORT).show();
//
//            printPDF();
//
//
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void createPDFFile(String pdfFilename) {
        if(new File(pdfFilename).exists())
            new File(pdfFilename).delete();
        try {
            OutputStream file = new FileOutputStream(new File(pdfFilename));
            Document document = new Document();
            PdfWriter.getInstance(document, file);

            //Save
            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("TTShop");
            document.addCreator("tenNhanVien");

            //Inserting Image in PDF
            Image image = Image.getInstance ("src/drawable/logo.jpg");//Header Image
            image.scaleAbsolute(540f, 72f);//image width,height

            PdfPTable irdTable = new PdfPTable(2);
            irdTable.addCell(getIRDCell("Invoice No"));
            irdTable.addCell(getIRDCell("Invoice Date"));
            irdTable.addCell(getIRDCell("XE1234")); // pass invoice number
            irdTable.addCell(getIRDCell("13-Mar-2016")); // pass invoice date

            PdfPTable irhTable = new PdfPTable(3);
            irhTable.setWidthPercentage(100);

            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("Invoice", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            PdfPCell invoiceTable = new PdfPCell (irdTable);
            invoiceTable.setBorder(0);
            irhTable.addCell(invoiceTable);

            FontSelector fs = new FontSelector();
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            fs.addFont(font);
            Phrase bill = fs.process("Bill To"); // customer information
            Paragraph name = new Paragraph("Mr.Venkateswara Rao");
            name.setIndentationLeft(20);
            Paragraph contact = new Paragraph("9652886877");
            contact.setIndentationLeft(20);
            Paragraph address = new Paragraph("Kuchipudi,Movva");
            address.setIndentationLeft(20);

            PdfPTable billTable = new PdfPTable(6); //one page contains 15 records
            billTable.setWidthPercentage(100);
            billTable.setWidths(new float[] { 1, 2,5,2,1,2 });
            billTable.setSpacingBefore(30.0f);
            billTable.addCell(getBillHeaderCell("Index"));
            billTable.addCell(getBillHeaderCell("Item"));
            billTable.addCell(getBillHeaderCell("Description"));
            billTable.addCell(getBillHeaderCell("Unit Price"));
            billTable.addCell(getBillHeaderCell("Qty"));
            billTable.addCell(getBillHeaderCell("Amount"));

            billTable.addCell(getBillRowCell("1"));
            billTable.addCell(getBillRowCell("Mobile"));
            billTable.addCell(getBillRowCell("Nokia Lumia 610 \n IMI:WQ361989213 "));
            billTable.addCell(getBillRowCell("12000.0"));
            billTable.addCell(getBillRowCell("1"));
            billTable.addCell(getBillRowCell("12000.0"));

            billTable.addCell(getBillRowCell("2"));
            billTable.addCell(getBillRowCell("Accessories"));
            billTable.addCell(getBillRowCell("Nokia Lumia 610 Panel \n Serial:TIN3720 "));
            billTable.addCell(getBillRowCell("200.0"));
            billTable.addCell(getBillRowCell("1"));
            billTable.addCell(getBillRowCell("200.0"));


            billTable.addCell(getBillRowCell("3"));
            billTable.addCell(getBillRowCell("Other"));
            billTable.addCell(getBillRowCell("16Gb Memorycard \n Serial:UR8531 "));
            billTable.addCell(getBillRowCell("420.0"));
            billTable.addCell(getBillRowCell("1"));
            billTable.addCell(getBillRowCell("420.0"));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell("Warranty"));
            validity.addCell(getValidityCell(" * Products purchased comes with 1 year national warranty \n   (if applicable)"));
            validity.addCell(getValidityCell(" * Warranty should be claimed only from the respective manufactures"));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setColspan (3);
            summaryL.setPadding (1.0f);
            billTable.addCell(summaryL);

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("Subtotal"));
            accounts.addCell(getAccountsCellR("12620.00"));
            accounts.addCell(getAccountsCell("Discount (10%)"));
            accounts.addCell(getAccountsCellR("1262.00"));
            accounts.addCell(getAccountsCell("Tax(2.5%)"));
            accounts.addCell(getAccountsCellR("315.55"));
            accounts.addCell(getAccountsCell("Total"));
            accounts.addCell(getAccountsCellR("11673.55"));
            PdfPCell summaryR = new PdfPCell (accounts);
            summaryR.setColspan (3);
            billTable.addCell(summaryR);

            PdfPTable describer = new PdfPTable(1);
            describer.setWidthPercentage(100);
            describer.addCell(getdescCell(" "));
            describer.addCell(getdescCell("Goods once sold will not be taken back or exchanged || Subject to product justification || Product damage no one responsible || "
                    + " Service only at concarned authorized service centers"));

            document.open();//PDF document opened........

            document.add(image);
            document.add(irhTable);
            document.add(bill);
            document.add(name);
            document.add(contact);
            document.add(address);
            document.add(billTable);
            document.add(describer);

            document.close();

            System.out.println("Pdf created successfully..");
            printPDF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setHeader() {

    }


    public static PdfPCell getIRHCell(String text, int alignment) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        /*	font.setColor(BaseColor.GRAY);*/
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPCell getIRDCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    public static PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        return cell;
    }

    public static PdfPCell getBillRowCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getBillFooterCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getValidityCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getAccountsCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setPadding (5.0f);
        return cell;
    }
    public static PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        cell.setPadding (5.0f);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getdescCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setBorder(0);
        return cell;
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(CustomerInfoActivity.this, Common.getAppPath(CustomerInfoActivity.this)+"test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());

        }
        catch (Exception e){
            Log.e("EDMTDev", ""+e.getMessage());
        }
    }

    private void addNewItemFull(Document document, String tenSp, String donGia, String soLuong, String thanhTien, Font bigFont, Font smallFont) throws DocumentException {
        Chunk chunkTenSP = new Chunk(tenSp, bigFont);
        Chunk chunkDonGia = new Chunk(donGia, smallFont);
        Chunk chunkSoLuong = new Chunk(soLuong, smallFont);
        Chunk chunkThanhTien = new Chunk(donGia, smallFont);
        Paragraph p = new Paragraph(chunkTenSP);
        p.add(new Chunk((new VerticalPositionMark())));
        p.add(chunkDonGia);
        p.add(chunkSoLuong);
        p.add(chunkThanhTien);
        document.add(p);

    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk((new VerticalPositionMark())));
        p.add(chunkTextRight);
        document.add(p);

    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException{
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }

    public void mapping(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Thông tin khách hàng");
        dialog = new Dialog(this);
        edtPhonNumber = (EditText) findViewById(R.id.sodienthoaiKH_info);
        edtName = (EditText) findViewById(R.id.hotenKH_info);
        edtAddress = (EditText) findViewById(R.id.diachiKH_info);
        edtEmail = (EditText) findViewById(R.id.emailKH_info);
        btnContinue = (Button) findViewById(R.id.btnContinue_info);
        btnUpdateCustomer = (Button) findViewById(R.id.btnUpdate_info);
        btnPrint = findViewById(R.id.btnPrint);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Customer");
        mData = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        discountId2 = CartActivity.discountId2;
        totalFinal = CartActivity.totalFinal;
        tenNhanVien = AccountActivity.tenNhanVien;
        arrayList = new ArrayList<>();
        viewOrderDetailAdapter = new ViewOrderDetailAdapter(getApplicationContext(), arrayList);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Intent intent1 = getIntent();
//        Bundle bundle = intent1.getExtras();
//        if (bundle != null) {
//            discountId2 = bundle.getString("discountId2");
//            totalFinal = bundle.getDouble("totalFinal");
//        }
//    }
}