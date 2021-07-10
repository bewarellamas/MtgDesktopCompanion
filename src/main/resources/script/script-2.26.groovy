if(dao.isSQL()) {
	printf("Executing db update on " + dao.getName());
	dao.executeQuery("ALTER TABLE contacts ADD contact_active boolean");
	dao.executeQuery("ALTER TABLE sealed ADD extra VARCHAR(10)");
	dao.executeQuery("ALTER TABLE sealed ADD collection VARCHAR(255)");
	dao.executeQuery("ALTER TABLE sealed ADD price DECIMAL");
	dao.executeQuery("ALTER TABLE cards ADD dateUpdate TIMESTAMP");
	printf("--done");
}
else
{
	printf("Your DAO is not SQL. Don't need to pass script");
}