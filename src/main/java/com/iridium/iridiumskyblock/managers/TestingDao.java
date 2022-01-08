package com.iridium.iridiumskyblock.managers;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.*;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.ObjectFactory;
import com.j256.ormlite.table.TableInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class TestingDao<T, ID> implements Dao<T, ID> {
    @Override
    public T queryForId(ID id) throws SQLException {
        return null;
    }

    @Override
    public T queryForFirst(PreparedQuery<T> preparedQuery) throws SQLException {
        return null;
    }

    @Override
    public List<T> queryForAll() throws SQLException {
        return null;
    }

    @Override
    public T queryForFirst() throws SQLException {
        return null;
    }

    @Override
    public List<T> queryForEq(String fieldName, Object value) throws SQLException {
        return null;
    }

    @Override
    public List<T> queryForMatching(T matchObj) throws SQLException {
        return null;
    }

    @Override
    public List<T> queryForMatchingArgs(T matchObj) throws SQLException {
        return null;
    }

    @Override
    public List<T> queryForFieldValues(Map<String, Object> fieldValues) throws SQLException {
        return null;
    }

    @Override
    public List<T> queryForFieldValuesArgs(Map<String, Object> fieldValues) throws SQLException {
        return null;
    }

    @Override
    public T queryForSameId(T data) throws SQLException {
        return null;
    }

    @Override
    public QueryBuilder<T, ID> queryBuilder() {
        return null;
    }

    @Override
    public UpdateBuilder<T, ID> updateBuilder() {
        return null;
    }

    @Override
    public DeleteBuilder<T, ID> deleteBuilder() {
        return null;
    }

    @Override
    public List<T> query(PreparedQuery<T> preparedQuery) throws SQLException {
        return null;
    }

    @Override
    public int create(T data) throws SQLException {
        return 0;
    }

    @Override
    public int create(Collection<T> datas) throws SQLException {
        return 0;
    }

    @Override
    public T createIfNotExists(T data) throws SQLException {
        return null;
    }

    @Override
    public CreateOrUpdateStatus createOrUpdate(T data) throws SQLException {
        return null;
    }

    @Override
    public int update(T data) throws SQLException {
        return 0;
    }

    @Override
    public int updateId(T data, ID newId) throws SQLException {
        return 0;
    }

    @Override
    public int update(PreparedUpdate<T> preparedUpdate) throws SQLException {
        return 0;
    }

    @Override
    public int refresh(T data) throws SQLException {
        return 0;
    }

    @Override
    public int delete(T data) throws SQLException {
        return 0;
    }

    @Override
    public int deleteById(ID id) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Collection<T> datas) throws SQLException {
        return 0;
    }

    @Override
    public int deleteIds(Collection<ID> ids) throws SQLException {
        return 0;
    }

    @Override
    public int delete(PreparedDelete<T> preparedDelete) throws SQLException {
        return 0;
    }

    @Override
    public CloseableIterator<T> iterator() {
        return null;
    }

    @Override
    public CloseableIterator<T> iterator(int resultFlags) {
        return null;
    }

    @Override
    public CloseableIterator<T> iterator(PreparedQuery<T> preparedQuery) throws SQLException {
        return null;
    }

    @Override
    public CloseableIterator<T> iterator(PreparedQuery<T> preparedQuery, int resultFlags) throws SQLException {
        return null;
    }

    @Override
    public CloseableWrappedIterable<T> getWrappedIterable() {
        return null;
    }

    @Override
    public CloseableWrappedIterable<T> getWrappedIterable(PreparedQuery<T> preparedQuery) {
        return null;
    }

    @Override
    public void closeLastIterator() throws IOException {

    }

    @Override
    public GenericRawResults<String[]> queryRaw(String query, String... arguments) throws SQLException {
        return null;
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String query, RawRowMapper<UO> mapper, String... arguments) throws SQLException {
        return null;
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String query, DataType[] columnTypes, RawRowObjectMapper<UO> mapper, String... arguments) throws SQLException {
        return null;
    }

    @Override
    public GenericRawResults<Object[]> queryRaw(String query, DataType[] columnTypes, String... arguments) throws SQLException {
        return null;
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String query, DatabaseResultsMapper<UO> mapper, String... arguments) throws SQLException {
        return null;
    }

    @Override
    public long queryRawValue(String query, String... arguments) throws SQLException {
        return 0;
    }

    @Override
    public int executeRaw(String statement, String... arguments) throws SQLException {
        return 0;
    }

    @Override
    public int executeRawNoArgs(String statement) throws SQLException {
        return 0;
    }

    @Override
    public int updateRaw(String statement, String... arguments) throws SQLException {
        return 0;
    }

    @Override
    public <CT> CT callBatchTasks(Callable<CT> callable) throws Exception {
        return null;
    }

    @Override
    public String objectToString(T data) {
        return null;
    }

    @Override
    public boolean objectsEqual(T data1, T data2) throws SQLException {
        return false;
    }

    @Override
    public ID extractId(T data) throws SQLException {
        return null;
    }

    @Override
    public Class<T> getDataClass() {
        return null;
    }

    @Override
    public FieldType findForeignFieldType(Class<?> clazz) {
        return null;
    }

    @Override
    public boolean isUpdatable() {
        return false;
    }

    @Override
    public boolean isTableExists() throws SQLException {
        return false;
    }

    @Override
    public long countOf() throws SQLException {
        return 0;
    }

    @Override
    public long countOf(PreparedQuery<T> preparedQuery) throws SQLException {
        return 0;
    }

    @Override
    public void assignEmptyForeignCollection(T parent, String fieldName) throws SQLException {

    }

    @Override
    public <FT> ForeignCollection<FT> getEmptyForeignCollection(String fieldName) throws SQLException {
        return null;
    }

    @Override
    public void setObjectCache(boolean enabled) throws SQLException {

    }

    @Override
    public void setObjectCache(ObjectCache objectCache) throws SQLException {

    }

    @Override
    public ObjectCache getObjectCache() {
        return null;
    }

    @Override
    public void clearObjectCache() {

    }

    @Override
    public T mapSelectStarRow(DatabaseResults results) throws SQLException {
        return null;
    }

    @Override
    public GenericRowMapper<T> getSelectStarRowMapper() throws SQLException {
        return null;
    }

    @Override
    public RawRowMapper<T> getRawRowMapper() {
        return null;
    }

    @Override
    public boolean idExists(ID id) throws SQLException {
        return false;
    }

    @Override
    public DatabaseConnection startThreadConnection() throws SQLException {
        return null;
    }

    @Override
    public void endThreadConnection(DatabaseConnection connection) throws SQLException {

    }

    @Override
    public void setAutoCommit(DatabaseConnection connection, boolean autoCommit) throws SQLException {

    }

    @Override
    public boolean isAutoCommit(DatabaseConnection connection) throws SQLException {
        return false;
    }

    @Override
    public void commit(DatabaseConnection connection) throws SQLException {

    }

    @Override
    public void rollBack(DatabaseConnection connection) throws SQLException {

    }

    @Override
    public ConnectionSource getConnectionSource() {
        return null;
    }

    @Override
    public void setObjectFactory(ObjectFactory<T> objectFactory) {

    }

    @Override
    public void registerObserver(DaoObserver observer) {

    }

    @Override
    public void unregisterObserver(DaoObserver observer) {

    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public void notifyChanges() {

    }

    @Override
    public T createObjectInstance() throws SQLException {
        return null;
    }

    @Override
    public TableInfo<T, ID> getTableInfo() {
        return null;
    }

    @Override
    public CloseableIterator<T> closeableIterator() {
        return null;
    }
}
