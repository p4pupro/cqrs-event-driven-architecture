// The getgetSiblingDB avoid that the creation fail
// and use database sales fail if it exits
db.getSiblingDB("sales").createCollection("devices");

db.getSiblingDB("sales").createUser({
  user: "query_user",
  pwd: "FNQXvAzNCew6YMkdnDAp79bpT4ub",
  roles: [
    { role: "readWrite", db: "sales" }
  ]
});
