const express = require("express");

const app = express();
app.use(express.json());

const records = [{ id: 1, name: "first record" }];

app.get("/ping", (req, res) => {
  res.json({ status: "ok" });
});

app.get("/records", (req, res) => {
  res.json(records);
});

app.post("/records", (req, res) => {
  const record = { id: records.length + 1, name: req.body.name };
  records.push(record);
  res.status(201).json(record);
});

if (require.main === module) {
  const port = process.env.PORT || 5000;
  app.listen(port, () => console.log(`MERN PoC running on port ${port}`));
}

module.exports = app;
