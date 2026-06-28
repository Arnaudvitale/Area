const request = require("supertest");
const app = require("./server");

describe("MERN PoC API", () => {
  it("answers on /ping", async () => {
    const res = await request(app).get("/ping");
    expect(res.statusCode).toBe(200);
    expect(res.body).toEqual({ status: "ok" });
  });

  it("lists records", async () => {
    const res = await request(app).get("/records");
    expect(res.statusCode).toBe(200);
    expect(Array.isArray(res.body)).toBe(true);
  });

  it("creates a record", async () => {
    const res = await request(app).post("/records").send({ name: "second" });
    expect(res.statusCode).toBe(201);
    expect(res.body.name).toBe("second");
  });
});
