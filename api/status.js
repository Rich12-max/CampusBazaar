module.exports = (req, res) => {
  res.setHeader('Content-Type', 'application/json');
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  res.status(200).json({
    status: "ONLINE",
    app: "CampusBazaar",
    version: "1.0.0",
    platform: "Vercel Serverless",
    timestamp: new Date().toISOString(),
    message: "Campus Peer-to-Peer Marketplace API is active"
  });
};
