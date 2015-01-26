package library;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AddBookFrame extends JFrame {

    private KeyValueComboBoxModel<String, String> publishers = new KeyValueComboBoxModel<String, String>();
    private KeyValueComboBoxModel<String, String> subjects = new KeyValueComboBoxModel<String, String>();
    private KeyValueComboBoxModel<String, String> authors = new KeyValueComboBoxModel<String, String>();

    private DefaultListModel dlm;

    private int pubId = -1;
    private int subId = -1;
    private int authId = -1;

    public static void main(String[] args) {
        AddBookFrame f = new AddBookFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public AddBookFrame() {
        setTitle("Добавление книги");
        initComponents();//инициализация компонентов
        initData();//инициализация данных
        initActions();//инициализация событий
    }

    private void initActions() {
        cbPublishers.addItemListener(new ItemListener() {
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

        cbSubjects.addItemListener(new ItemListener() {
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

        cbAllAuthors.addItemListener(new ItemListener() {
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

        btAddAuthor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (authId == -1) {
                    System.out.println("Выберите автора для добавления");
                } else {
                    dlm.addElement(authors.getSelectedValue());
//                    authId = -1;
                }
            }
        });

        btCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btAddBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("".equals(tfBookName.getText())) {
                    JOptionPane.showMessageDialog(null, "Введите название книги!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                } else if (subId == -1) {
                    JOptionPane.showMessageDialog(null, "Выберите тематику книги!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                } else if (pubId == -1) {
                    JOptionPane.showMessageDialog(null, "Выберите издательство книги!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                } else if ("".equals(tfPubYear.getText())) {
                    JOptionPane.showMessageDialog(null, "Введите год публикации книги!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                } else if ("".equals(tfShelfNumber.getText())) {
                    JOptionPane.showMessageDialog(null, "Введите номер полки книги!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                } else if ("".equals(tfPrice.getText())) {
                    JOptionPane.showMessageDialog(null, "Введите цену книги!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                } else if ("".equals(tfCount.getText())) {
                    JOptionPane.showMessageDialog(null, "Введите количество книг!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                } else if (dlm.getSize() == 0) {
                    JOptionPane.showMessageDialog(null, "Добавьте хотя бы одного автора книги!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("-----OK-----");

                    try {
                        Connection conn = ConnectionToDB.getConnection();
                        try {
                            Statement stat = conn.createStatement();

                            stat.executeUpdate(""
                                    + "INSERT INTO Books "
                                    + "(bookId, "
                                    + "bookName, "
                                    + "publisherId, "
                                    + "subjectId, "
                                    + "shelfNumber, "
                                    + "pubYear, "
                                    + "price, "
                                    + "countInStock) "
                                    + "VALUES ("
                                    + "(select case when (max(bookId) is not null) then max(bookId) + 1 else 1 end from Books), "
                                    + "'" + tfBookName.getText() + "', "
                                    + "" + pubId + ", "
                                    + "" + subId + ", "
                                    + "" + tfShelfNumber.getText() + ", "
                                    + "" + tfPubYear.getText() + ", "
                                    + "" + tfPrice.getText() + ", "
                                    + "" + tfCount.getText() + ")");

                            for (int i = 0; i < dlm.size(); i++) {
                                stat.executeUpdate(""
                                        + "INSERT INTO BooksAuthors "
                                        + "(bookId, "
                                        + "authorId) "
                                        + "VALUES ((select max(bookId) from Books), "
                                        + "(select authorId from Authors where authorName = '" + dlm.get(i).toString() + "'))");
                            }

                            JOptionPane.showMessageDialog(null, "Книга успешно добавлена!", "УРААА!", JOptionPane.INFORMATION_MESSAGE);

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
    }

    private void initData() {
        setSubjects();
        setPublishers();
        setAuthors();
    }

    private void setSubjects() {
        cbSubjects.setModel(subjects);
        cbSubjects.setRenderer(new ComboBoxRenderer());

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

    private void setPublishers() {
        cbPublishers.setModel(publishers);
        cbPublishers.setRenderer(new ComboBoxRenderer());

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

    private void setAuthors() {
        cbAllAuthors.setModel(authors);
        cbAllAuthors.setRenderer(new ComboBoxRenderer());

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

        lbBookName = new javax.swing.JLabel();
        tfBookName = new javax.swing.JTextField();
        lbSubjects = new javax.swing.JLabel();
        cbSubjects = new javax.swing.JComboBox();
        lbPublishers = new javax.swing.JLabel();
        cbPublishers = new javax.swing.JComboBox();
        lbPubYear = new javax.swing.JLabel();
        lbShelfNumber = new javax.swing.JLabel();
        lbPrice = new javax.swing.JLabel();
        lbCount = new javax.swing.JLabel();
        tfPubYear = new javax.swing.JTextField();
        tfShelfNumber = new javax.swing.JTextField();
        tfPrice = new javax.swing.JTextField();
        tfCount = new javax.swing.JTextField();
        spListBookAuthors = new javax.swing.JScrollPane();
        ltbBookAuthors = new javax.swing.JList();
        cbAllAuthors = new javax.swing.JComboBox();
        btAddAuthor = new javax.swing.JButton();
        lbBookAuthors = new javax.swing.JLabel();
        lbAllAuthors = new javax.swing.JLabel();
        btAddBook = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();

        dlm = new DefaultListModel();
        ltbBookAuthors.setModel(dlm);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbBookName.setText("Название книги:");

        tfBookName.setName(""); // NOI18N

        lbSubjects.setText("Тематика:");

//        cbSubjects.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lbPublishers.setText("Издательство:");

//        cbPublishers.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lbPubYear.setText("Год публикации:");

        lbShelfNumber.setText("Номер полки:");

        lbPrice.setText("Цена:");

        lbCount.setText("Количество:");

//        ltbBookAuthors.setModel(new javax.swing.AbstractListModel() {
//            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
//            public int getSize() { return strings.length; }
//            public Object getElementAt(int i) { return strings[i]; }
//        });
        spListBookAuthors.setViewportView(ltbBookAuthors);

//        cbAllAuthors.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        btAddAuthor.setText(">");

        lbBookAuthors.setText("Авторы книги:");

        lbAllAuthors.setText("Все авторы:");

        btAddBook.setText("Добавить");

        btCancel.setText("Отмена");

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
                                        .addComponent(tfBookName))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbSubjects)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbSubjects, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbPublishers)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbPublishers, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbPubYear)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfPubYear, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbShelfNumber)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfShelfNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbPrice)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbCount)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfCount, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(cbAllAuthors, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btAddAuthor))
                                                .addComponent(lbAllAuthors))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(spListBookAuthors)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(lbBookAuthors)
                                                        .addGap(0, 0, Short.MAX_VALUE))))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(btAddBook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbBookName)
                                .addComponent(tfBookName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbSubjects)
                                .addComponent(cbSubjects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbPublishers)
                                .addComponent(cbPublishers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbPubYear)
                                .addComponent(tfPubYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfShelfNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbPrice)
                                .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbCount)
                                .addComponent(tfCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbShelfNumber))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbAllAuthors)
                                .addComponent(lbBookAuthors))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btAddAuthor)
                                        .addComponent(cbAllAuthors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(spListBookAuthors, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btAddBook)
                                .addComponent(btCancel))
                        .addContainerGap())
        );

        pack();
    }

    private javax.swing.JButton btAddAuthor;
    private javax.swing.JButton btAddBook;
    private javax.swing.JButton btCancel;
    private javax.swing.JComboBox cbSubjects;
    private javax.swing.JComboBox cbPublishers;
    private javax.swing.JComboBox cbAllAuthors;
    private javax.swing.JLabel lbBookName;
    private javax.swing.JLabel lbSubjects;
    private javax.swing.JLabel lbPublishers;
    private javax.swing.JLabel lbPubYear;
    private javax.swing.JLabel lbShelfNumber;
    private javax.swing.JLabel lbPrice;
    private javax.swing.JLabel lbCount;
    private javax.swing.JLabel lbAllAuthors;
    private javax.swing.JLabel lbBookAuthors;
    private javax.swing.JList ltbBookAuthors;
    private javax.swing.JScrollPane spListBookAuthors;
    private javax.swing.JTextField tfBookName;
    private javax.swing.JTextField tfPubYear;
    private javax.swing.JTextField tfShelfNumber;
    private javax.swing.JTextField tfPrice;
    private javax.swing.JTextField tfCount;

}
