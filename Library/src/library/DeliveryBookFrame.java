package library;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DeliveryBookFrame extends JFrame {
    
    private KeyValueComboBoxModel<String, String> readers = new KeyValueComboBoxModel<String, String>();
    private int readerId = -1;

    private int bookId;
    private String bookName;
    private String pubYear;
    private String publisherName;
    private String delDate;
    
    public static void main(String[] args) {
        DeliveryBookFrame dbf = new DeliveryBookFrame(1, "Книга1", "Издательство1", "2011");
        dbf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dbf.setVisible(true);
    }
    
    public DeliveryBookFrame(int bookId, String bookName, String publisherName, String pubYear) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.pubYear = pubYear;
        this.publisherName = publisherName;
        this.delDate = new SimpleDateFormat("dd.MM.yyyy").format(new GregorianCalendar().getTime());//текущая дата
        
        initComponents();//инициализация компонентов
        initData();//инициализация данных
        initActions();
    }
    
    private void initActions()
    {
        btDelivery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("".equals(tfDayCount.getText()))
                    JOptionPane.showMessageDialog(null, "Выберите количество дней!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                else if (readerId == -1)
                    JOptionPane.showMessageDialog(null, "Выберите читателя!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                else
                {
                    try {
                        Connection conn = ConnectionToDB.getConnection();
                        try {
                            Statement stat = conn.createStatement();

                            stat.executeUpdate(""
                                    + "INSERT INTO Delivery ("
                                        + "readerId, "
                                        + "bookId, "
                                        + "receiptDate, "
                                        + "returnDate) "
                                    + "VALUES ("
                                        + "" + readerId + ", "
                                        + "" + bookId + ", "
                                        + "now()::date, "
                                        + "now()::date + " + tfDayCount.getText() + ")");

                            stat.executeUpdate(""
                                    + "UPDATE Books "
                                    + "SET countInStock = countInStock - 1"
                                    + "WHERE bookId = " + bookId + "");

                            JOptionPane.showMessageDialog(null, "Книга успешно выдана!", "УРААА!", JOptionPane.INFORMATION_MESSAGE);

                            dispose();
                        } finally {
                            conn.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        cbReaders.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    try {
                        readerId = Integer.parseInt(readers.getSelectedKey());
                    } catch (Exception ex) {
                        readerId = -1;
                        System.out.println("ОШИБКА ПРЕОБРАЗОВАНИЯ КОДА ЧИТАТЕЛЯ В ЧИСЛОВОЙ ТИП!");
                    }
                }
            }
        });
        
        btCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void initData() {
        setReaders();
    }
    
    private void setReaders() {
        cbReaders.setModel(readers);
        cbReaders.setRenderer(new ComboBoxRenderer());

        try {
            Connection conn = ConnectionToDB.getConnection();
            try {
                Statement stat = conn.createStatement();
                ResultSet res = stat.executeQuery(""
                        + "SELECT readerId, readerName "
                        + "FROM Readers");

                readers.clear();
                readers.put("-1", "");

                while (res.next()) {
                    readers.put(Integer.toString(res.getInt("readerId")), res.getString("readerName"));
                }
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void initComponents() {

//        jTextField1 = new javax.swing.JTextField();
        lbBookName = new javax.swing.JLabel();
        lbBookNameValue = new javax.swing.JLabel();
        lbPublisher = new javax.swing.JLabel();
        lbPublisherValue = new javax.swing.JLabel();
        lbpubYear = new javax.swing.JLabel();
        lbpubYearValue = new javax.swing.JLabel();
        lbDeliveryDate = new javax.swing.JLabel();
        lbDeliveryDateValue = new javax.swing.JLabel();
        lbDayCount = new javax.swing.JLabel();
        lbReaders = new javax.swing.JLabel();
        cbReaders = new javax.swing.JComboBox();
        btDelivery = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        tfDayCount = new javax.swing.JTextField();

//        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbBookName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbBookName.setText("Название книги:");

        lbBookNameValue.setText(bookName);

        lbPublisher.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbPublisher.setText("Издательство:");

        lbPublisherValue.setText(publisherName);

        lbpubYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbpubYear.setText("Год издания:");

        lbpubYearValue.setText(pubYear);

        lbDeliveryDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbDeliveryDate.setText("Дата выдачи:");

        lbDeliveryDateValue.setText(delDate);

        lbDayCount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbDayCount.setText("Кол-во дней:");

        lbReaders.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbReaders.setText("Читатель:");

//        cbReaders.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btDelivery.setText("Выдать");
        

        btCancel.setText("Отмена");
        

        tfDayCount.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbBookName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbBookNameValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lbPublisher)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPublisherValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbpubYear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbpubYearValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbDeliveryDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbDeliveryDateValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbDayCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfDayCount, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbReaders)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbReaders, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btDelivery, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbBookName)
                    .addComponent(lbBookNameValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPublisher)
                    .addComponent(lbPublisherValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbpubYear)
                    .addComponent(lbpubYearValue)
                    .addComponent(lbDeliveryDate)
                    .addComponent(lbDeliveryDateValue)
                    .addComponent(lbDayCount)
                    .addComponent(tfDayCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbReaders)
                    .addComponent(cbReaders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btDelivery)
                    .addComponent(btCancel))
                .addContainerGap())
        );

        pack();
    }
    
    private javax.swing.JButton btDelivery;
    private javax.swing.JButton btCancel;
    private javax.swing.JComboBox cbReaders;
    private javax.swing.JLabel lbBookName;
    private javax.swing.JLabel lbReaders;
    private javax.swing.JLabel lbBookNameValue;
    private javax.swing.JLabel lbPublisher;
    private javax.swing.JLabel lbPublisherValue;
    private javax.swing.JLabel lbpubYear;
    private javax.swing.JLabel lbpubYearValue;
    private javax.swing.JLabel lbDeliveryDate;
    private javax.swing.JLabel lbDeliveryDateValue;
    private javax.swing.JLabel lbDayCount;
    private javax.swing.JTextField tfDayCount;
}
