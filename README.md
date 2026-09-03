# RoadWatch 🚦

**RoadWatch** is a location-aware, community-driven road condition reporting platform designed to help people discover and share **real-time local road information**.

> **Know the Road. Share the Road. Make Every Journey Safer.**

People already on the road can report what is happening around them, while nearby users can discover relevant road updates based on their location.

RoadWatch is being developed with a strong focus on **production-grade backend engineering, distributed systems, scalability, reliability, security, and failure handling** rather than simply building a CRUD application.

---

## 🌐 Live Application

### Frontend

**RoadWatch Web App**

https://roadwatch-gp5h.onrender.com

The frontend is deployed as a React/Vite application.

---

# 🎯 Core Idea

A user can create a road-related report containing:

- Description
- Category
- Location
- Images/videos

Other users can then discover reports near their current location.

```text
User Location
      ↓
Latitude + Longitude
      ↓
Post Service
      ↓
Geographical Distance Calculation
      ↓
Nearby Reports
      ↓
Relevant Local Road Updates
