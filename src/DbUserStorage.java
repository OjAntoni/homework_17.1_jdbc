import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbUserStorage {

    private Connection connection;

    public DbUserStorage(Connection connection) {
        this.connection = connection;
    }


    public void save(User user) {
        try {
            if (existsById(user.getId())) return;
            connection.setAutoCommit(false);
            int dBuserId;
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users values (default, ?, ?, ?) returning id");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getPassword());
            ResultSet resultSet1 = preparedStatement.executeQuery();
            resultSet1.next();
            dBuserId = resultSet1.getInt(1);

            List<Phone> userPhones = user.getPhones();
            ArrayList<Integer> phonesId = new ArrayList<>();
            PreparedStatement preparedStatement1 = connection.prepareStatement("insert into phones values (default ,?) returning phone_id");
            for (Phone phone : userPhones) {
                preparedStatement1.setString(1, phone.getNumber());
                ResultSet resultSet2 = preparedStatement1.executeQuery();
                resultSet2.next();
                phonesId.add(resultSet2.getInt(1));
            }

            ArrayList<Address> addresses = user.getAddresses();
            ArrayList<Integer> addressesId = new ArrayList<>();
            PreparedStatement preparedStatement2 = connection.prepareStatement("insert into addresses values (default , ?) returning address_id");
            for (Address address : addresses) {
                preparedStatement2.setString(1, address.getStreet());
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                resultSet2.next();
                addressesId.add(resultSet2.getInt(1));
            }

            PreparedStatement preparedStatement3 = connection.prepareStatement("insert into users_phones values (" + dBuserId + ",?)");
            for (int phoneId : phonesId) {
                preparedStatement3.setInt(1, phoneId);
                preparedStatement3.execute();
            }

            PreparedStatement preparedStatement4 = connection.prepareStatement("insert into users_addresses values (" + dBuserId + ",?)");
            for (int adressId : addressesId) {
                preparedStatement4.setInt(1, adressId);
                preparedStatement4.execute();
            }

            connection.commit();

        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void updateNameById(int id, String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update users set name = ? where id = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updatePasswordById(int id, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update users set password = ? where id = ?");
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement usersPreparedStatement = connection.prepareStatement("delete from users where id=" + id);
            usersPreparedStatement.execute();

            PreparedStatement preparedStatement = connection.prepareStatement("select up.phone_id from users_phones up where up.user_id=" + id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> phonesId = new ArrayList<>();
            while (resultSet.next()) {
                phonesId.add(resultSet.getInt(1));
            }
            PreparedStatement preparedStatement1 = connection.prepareStatement("delete from phones p where p.phone_id=?");
            for (int p_id : phonesId) {
                preparedStatement1.setInt(1, p_id);
                preparedStatement1.execute();
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("select ua.address_id from users_addresses ua where ua.user_id=" + id);
            ResultSet resultSet1 = preparedStatement2.executeQuery();
            ArrayList<Integer> addressesId = new ArrayList<>();
            while (resultSet1.next()) {
                addressesId.add(resultSet1.getInt(1));
            }
            PreparedStatement preparedStatement3 = connection.prepareStatement("DELETE FROM addresses a WHERE a.address_id=?");
            for (int a_id : addressesId) {
                preparedStatement3.setInt(1, a_id);
                preparedStatement3.execute();
            }

            connection.prepareStatement("delete from users_addresses ua where ua.user_id=" + id).execute();
            connection.prepareStatement("delete from users_phones up where up.user_id=" + id).execute();
            connection.commit();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public ArrayList<User> getAll() {
        try {
            return getUsersByCondition(connection.prepareStatement("select u.id from users u"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<User>();
    }

    public ArrayList<User> getAllByName(String userName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select u.id from users u where u.name=?");
            preparedStatement.setString(1,userName);
            return getUsersByCondition(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<User>();
    }

    public ArrayList<User> getAllBySurname(String surname){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select u.id from users u where u.surname=?");
            preparedStatement.setString(1,surname);
            return getUsersByCondition(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<User>();
    }

    public boolean exists(User user){
        return existsById(user.getId());
    }

    public boolean existsById(int id) {
        boolean answer = false;
        try {
            PreparedStatement preparedStatement0 = connection.prepareStatement("select u.id from users u where u.id=" + id);
            ResultSet resultSet = preparedStatement0.executeQuery();
            if (resultSet.next()) {
                answer = true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return answer;
    }

    private ArrayList<User> getUsersByCondition(PreparedStatement preparedStatementOnUsers){
        ArrayList<User> users = new ArrayList<>();
        try {
            ResultSet resultSet = preparedStatementOnUsers.executeQuery();
            ArrayList<Integer> usersId = new ArrayList<>();
            while (resultSet.next()) {
                usersId.add(resultSet.getInt(1));
            }
            PreparedStatement preparedStatementP = connection.prepareStatement("select p.* from phones p " +
                    "                        join (select up.phone_id from users_phones up where up.user_id=?) tt on (tt.phone_id=p.phone_id)");
            PreparedStatement preparedStatementA = connection.prepareStatement("select a.* from addresses a " +
                    "                      join (select ua.address_id from users_addresses ua where ua.user_id=?) tt on (tt.address_id=a.address_id);");
            PreparedStatement preparedStatementU = connection.prepareStatement("select u.* from users u where u.id=?");
            ArrayList<Phone> phones;
            ArrayList<Address> addresses;

            for (int user_id : usersId) {
                preparedStatementU.setInt(1, user_id);
                preparedStatementP.setInt(1, user_id);
                preparedStatementA.setInt(1, user_id);
                ResultSet resultSetU = preparedStatementU.executeQuery();
                ResultSet resultSetP = preparedStatementP.executeQuery();
                ResultSet resultSetA = preparedStatementA.executeQuery();
                phones = new ArrayList<>();
                addresses = new ArrayList<>();
                while (resultSetP.next()) {
                    phones.add(new Phone(resultSetP.getInt(1), resultSetP.getString(2)));
                }
                while (resultSetA.next()) {
                    addresses.add(new Address(resultSetA.getInt(1), resultSetA.getString(2)));
                }
                resultSetU.next();
                users.add(new User(resultSetU.getInt(1), resultSetU.getString(2), resultSetU.getString(3),
                        resultSetU.getString(4), phones, addresses));


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }
}

