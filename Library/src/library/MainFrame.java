package library;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
    
    private KeyValueComboBoxModel<String, String> publishers = new KeyValueComboBoxModel<String, String>();
    private KeyValueComboBoxModel<String, String> subjects = new KeyValueComboBoxModel<String, String>();
    private KeyValueComboBoxModel<String, String> authors = new KeyValueComboBoxModel<String, String>();
    
    private int pubId = -1;
    private int subId = -1;
    private int authId = -1;
    
    
    public MainFrame()
    {
        initComponents();//инициализация компонентов
        initData();//инициализация данных
        initActions();
    }
    
    public void initActions()
    {
        cbPublisher.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    try {
                        pubId = Integer.parseInt(publishers.getSelectedKey());
                    } catch (Exception ex) {
                        pubId = -1;
                        System.out.println("ОШИБКА ПРЕОБРАЗОВАНИЯ КОДА ИЗДАТЕЛЬСТВА В ЧИСЛОВОЙ ТИП!");
                    }
                }
            }
        });
        
        cbSubject.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    try {
                        subId = Integer.parseInt(subjects.getSelectedKey());
                    } catch (Exception ex) {
                        subId = -1;
                        System.out.println("ОШИБКА ПРЕОБРАЗОВАНИЯ КОДА ТЕМАТИКИ В ЧИСЛОВОЙ ТИП!");
                    }
                }
            }
        });
        
        cbAuthor.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    try {
                        authId = Integer.parseInt(authors.getSelectedKey());
                    } catch (Exception ex) {
                        authId = -1;
                        System.out.println("ОШИБКА ПРЕОБРАЗОВАНИЯ КОДА АВТОРА В ЧИСЛОВОЙ ТИП!");
                    }
                }
            }
        });
        
        btFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                
                
                
                
            }
        });
    }
    
    private void initData()
    {
        setPublisher();
        setSubject();
        setAuthor();
    }
    
    private void setPublisher()
    {
        cbPublisher.setModel(publishers);
        cbPublisher.setRenderer(new ComboBoxRenderer());
        
        try {
            Connection conn = ConnectionToDB.getConnection();
            try {
                Statement stat = conn.createStatement();
                ResultSet res = stat.executeQuery(""
                        + "SELECT publisherId, publisherName "
                        + "FROM Publishers");
                
                publishers.clear();
                publishers.put("-1", "");

                while (res.next()) {
                    publishers.put(Integer.toString(res.getInt("publisherId")), res.getString("publisherName"));
                }
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void setSubject()
    {
        cbSubject.setModel(subjects);
        cbSubject.setRenderer(new ComboBoxRenderer());
        
        try {
            Connection conn = ConnectionToDB.getConnection();
            try {
                Statement stat = conn.createStatement();
                ResultSet res = stat.executeQuery(""
                        + "SELECT subjectId, subjectName "
                        + "FROM Subjects");
                
                subjects.clear();
                subjects.put("-1", "");

                while (res.next()) {
                    subjects.put(Integer.toString(res.getInt("subjectId")), res.getString("subjectName"));
                }
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void setAuthor()
    {
        cbAuthor.setModel(authors);
        cbAuthor.setRenderer(new ComboBoxRenderer());
        
        try {
            Connection conn = ConnectionToDB.getConnection();
            try {
                Statement stat = conn.createStatement();
                ResultSet res = stat.executeQuery(""
                        + "SELECT authorId, authorName "
                        + "FROM Authors");
                
                authors.clear();
                authors.put("-1", "");

                while (res.next()) {
                    authors.put(Integer.toString(res.getInt("authorId")), res.getString("authorName"));
                }
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void initComponents() {

        lbFilters = new javax.swing.JLabel();
        lbPublisher = new javax.swing.JLabel();
        cbPublisher = new javax.swing.JComboBox();
        lbSubject = new javax.swing.JLabel();
        cbSubject = new javax.swing.JComboBox();
        lbAuthor = new javax.swing.JLabel();
        cbAuthor = new javax.swing.JComboBox();
        chbInStock = new javax.swing.JCheckBox();
        sep = new javax.swing.JSeparator();
        spTable = new javax.swing.JScrollPane();
        tbBooks = new javax.swing.JTable();
        btAddBook = new javax.swing.JButton();
        btGiveBook = new javax.swing.JButton();
        btSaleBook = new javax.swing.JButton();
        btFind = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Информационная система библиотеки");

        lbFilters.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbFilters.setText("Фильтры:");

        lbPublisher.setText("Издательство:");

//        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbSubject.setText("Тематика:");

//        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbAuthor.setText("Авторы:");

//        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        chbInStock.setText("В наличии");
        

        tbBooks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Шифр книги", "Название книги", "Номер полки", "Цена", "Кол-во в наличии"
            }
        ));
        spTable.setViewportView(tbBooks);

        btAddBook.setText("Добавить книгу");
        

        btGiveBook.setText("Выдать книгу");
        

        btSaleBook.setText("Продать книгу");
        

        btFind.setText("НАЙТИ");
        

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spTable, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sep, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbPublisher)
                            .addComponent(lbFilters))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chbInStock)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btFind))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbPublisher, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbSubject)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbSubject, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbAuthor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbAuthor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btAddBook, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                        .addGap(150, 150, 150)
                        .addComponent(btSaleBook, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                        .addGap(142, 142, 142)
                        .addComponent(btGiveBook, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbFilters)
                    .addComponent(chbInStock)
                    .addComponent(btFind))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPublisher)
                    .addComponent(cbPublisher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbSubject)
                    .addComponent(cbSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbAuthor)
                    .addComponent(cbAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sep, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spTable, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAddBook)
                    .addComponent(btGiveBook)
                    .addComponent(btSaleBook))
                .addContainerGap())
        );

        pack();
    }
    
    private javax.swing.JButton btAddBook;
    private javax.swing.JButton btGiveBook;
    private javax.swing.JButton btSaleBook;
    private javax.swing.JButton btFind;
    private javax.swing.JCheckBox chbInStock;
    private javax.swing.JComboBox cbPublisher;
    private javax.swing.JComboBox cbSubject;
    private javax.swing.JComboBox cbAuthor;
    private javax.swing.JLabel lbFilters;
    private javax.swing.JLabel lbPublisher;
    private javax.swing.JLabel lbSubject;
    private javax.swing.JLabel lbAuthor;
    private javax.swing.JScrollPane spTable;
    private javax.swing.JSeparator sep;
    private javax.swing.JTable tbBooks;
    //Publishers
}
