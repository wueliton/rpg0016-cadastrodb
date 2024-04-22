package cadastro.model;

import cadastro.model.util.ConectorBD;
import cadastro.model.util.SequenceManager;
import cadastrodb.model.PessoaFisica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaFisicaDAO {
    private ConectorBD conectorBD;
    private SequenceManager sequenceManager;

    public PessoaFisicaDAO(ConectorBD conectorBD, SequenceManager sequenceManager) {
        this.conectorBD = conectorBD;
        this.sequenceManager = sequenceManager;
    }

    public PessoaFisica getPessoa(int id) {
        PessoaFisica pessoaFisica = null;
        String sql = "SELECT * FROM pessoa AS p INNER JOIN pessoaFisica AS pf ON p.idPessoa = pf.idPessoa WHERE p.idPessoa = ?";
        try {
            PreparedStatement preparedStatement = conectorBD.getPrepared(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.first()) {
                pessoaFisica = converterPessoa(resultSet);
            }
            conectorBD.close(preparedStatement);
            conectorBD.close(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoaFisica;
    }

    public List<PessoaFisica> getPessoas() {
        List<PessoaFisica> pessoas = new ArrayList<PessoaFisica>();
        String sql = "SELECT * FROM pessoa AS p INNER JOIN pessoaFisica AS pf ON p.idPessoa = pf.idPessoa";
        try(ResultSet resultSet = conectorBD.getSelect(sql)) {
            while(resultSet.next()) {
                pessoas.add(converterPessoa(resultSet));
            }
            conectorBD.close(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoas;
    }

    public PessoaFisica inserir(PessoaFisica pessoa) {
        try {
            int idPessoa = sequenceManager.getValue("idPessoa");
            pessoa.setId(idPessoa);
            String sql = "INSERT INTO pessoa (idPessoa, nome, logradouro, cidade, estado, telefone, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conectorBD.getPrepared(sql);
            preparedStatement.setInt(1, idPessoa);
            preparedStatement.setString(2, pessoa.getNome());
            preparedStatement.setString(3, pessoa.getLogradouro());
            preparedStatement.setString(4, pessoa.getCidade());
            preparedStatement.setString(5, pessoa.getEstado());
            preparedStatement.setString(6, pessoa.getTelefone());
            preparedStatement.setString(7, pessoa.getEmail());
            preparedStatement.executeUpdate();
            conectorBD.close(preparedStatement);

            String sqlPF = "INSERT INTO pessoaFisica (idPessoa, cpf) VALUES (?, ?)";
            PreparedStatement preparedStatementPF = conectorBD.getPrepared(sqlPF);
            preparedStatementPF.setInt(1, idPessoa);
            preparedStatementPF.setString(2, pessoa.getCpf());
            preparedStatementPF.executeUpdate();
            conectorBD.close(preparedStatementPF);

            System.out.println("Pessoa Física salva com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoa;
    }

    public void alterar(PessoaFisica pessoa) {
        try {
            String sql = "UPDATE pessoa SET nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE idPessoa = ?";
            PreparedStatement preparedStatement = conectorBD.getPrepared(sql);
            preparedStatement.setString(1, pessoa.getNome());
            preparedStatement.setString(2, pessoa.getLogradouro());
            preparedStatement.setString(3, pessoa.getCidade());
            preparedStatement.setString(4, pessoa.getEstado());
            preparedStatement.setString(5, pessoa.getTelefone());
            preparedStatement.setString(6, pessoa.getEmail());
            preparedStatement.setInt(7, pessoa.getId());
            preparedStatement.executeUpdate();
            conectorBD.close(preparedStatement);

            String sqlPF = "UPDATE pessoaFisica SET cpf = ? WHERE idPessoa = ?";
            PreparedStatement preparedStatementPF = conectorBD.getPrepared(sqlPF);
            preparedStatementPF.setString(1, pessoa.getCpf());
            preparedStatementPF.setInt(2, pessoa.getId());
            preparedStatementPF.executeUpdate();
            conectorBD.close(preparedStatementPF);

            System.out.println("Pessoa Física atualizada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        try {
            String sqlPF = "DELETE FROM pessoaFisica WHERE idPessoa = ?";
            PreparedStatement preparedStatementPF = conectorBD.getPrepared(sqlPF);
            preparedStatementPF.setInt(1, id);
            preparedStatementPF.executeUpdate();

            String sql = "DELETE FROM pessoa WHERE idPessoa = ?";
            PreparedStatement preparedStatement = conectorBD.getPrepared(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            conectorBD.close(preparedStatement);

            System.out.println("Pessoa Física excluída com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PessoaFisica converterPessoa(ResultSet resultSet) throws SQLException {
        PessoaFisica pessoa = new PessoaFisica();
        pessoa.setId(resultSet.getInt("idPessoa"));
        pessoa.setNome(resultSet.getString("nome"));
        pessoa.setLogradouro(resultSet.getString("logradouro"));
        pessoa.setCidade(resultSet.getString("cidade"));
        pessoa.setEstado(resultSet.getString("estado"));
        pessoa.setTelefone(resultSet.getString("telefone"));
        pessoa.setEmail(resultSet.getString("email"));
        pessoa.setCpf(resultSet.getString("cpf"));
        return pessoa;
    }
}
