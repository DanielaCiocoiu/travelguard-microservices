db.roles.insertMany([
   { name: "ROLE_USER" },
   { name: "ROLE_MODERATOR" },
   { name: "ROLE_ADMIN" },
])

db.users.insertMany([
   { username: "Vasia", email: "Vasia@gmail.com", password: "12345678" },
   { username: "Ina", email: "Ina@gmail.com", password: "12345678" },
   { username: "Maia", email: "Maia@gmail.com", password: "12345678" },
])


