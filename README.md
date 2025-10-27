## DevSecOps Pipeline Implementation for Java Calculator App
<img width="1696" height="608" alt="image" src="https://github.com/user-attachments/assets/f68f743b-4c4e-491a-94c8-b2388725a05e" />


<img width="1423" height="653" alt="Screenshot 2025-10-26 at 4 06 27 PM" src="https://github.com/user-attachments/assets/caae9604-7126-4fd7-acaf-b8c3aa785b78" />

Features

✨ Simple web calculator built with Java and HTML/CSS/JS

⚡ REST API supports four operations: Add, Subtract, Multiply, Divide

📄 Modern, responsive UI

🔐 DevSecOps pipeline with unit tests, code quality, SCA, container image scanning

🚀 Automatic Docker image build, vulnerability scan, and push to GHCR

☸️ GitOps deployment: ArgoCD updates Kubernetes deployments from changed manifests

## Technologies Used
``` bash
Java 17 (Maven for build/test)

Docker, GitHub Actions

Kubernetes (deployment manifests), NGINX Ingress

Trivy for container scanning

ArgoCD for GitOps

HTML, CSS (modern look, fully responsive for desktop/mobile)
```
## Project Structure
```bash
src/
├── main/
│   └── java/
│       └── com/example/calc/
│           ├── CalculatorServer.java  # HTTP Server entry point
│           ├── CalculatorHandler.java # Handles calculations
│           └── package-info.java      # Javadoc info
├── test/
│   └── java/
│       └── com/example/calc/
│           └── CalculatorTest.java    # Unit tests for calculator logic
Dockerfile                             # Container build for app
.github/
└── workflows/
    └── ci-cd.yml                      # Complete DevSecOps pipeline
kubernetes/
├── deployment.yaml
├── service.yaml
└── ingress.yaml
index.html                             # UI for calculator in container
```
## Calculator App Logic
Accepts two numbers and an operation (add, subtract, multiply, divide).

Performs calculation and returns/display result instantly.

Handles divide-by-zero and other edge cases gracefully.

Getting Started
Prerequisites
JDK 17+ (for building/running locally)

Maven

## Docker

Node.js (optional, only for advanced/test scripts)

Kubernetes cluster and kubectl for deployment

Installation & Local Run
Clone the repository:

```bash
git clone https://github.com/Bharathi-vbr/DevSecOps_Demo.git
cd DevSecOps_Demo
```
## Build and run locally:

```bash
mvn clean package
java -cp target/*.jar com.example.calc.CalculatorServer
```
# Access at http://localhost:8080/
OR Build and run with Docker:

```bash
docker build -t calculator-app .
docker run -p 8080:8080 calculator-app
```
# Access at http://localhost:8080/
## Building for Production
Build a Docker image (GitHub Actions will do this automatically):

```bash
docker build -t ghcr.io/bharathi-vbr/devsecops_demo:<tag> .
docker push ghcr.io/bharathi-vbr/devsecops_demo:<tag>
Deploying to Kubernetes
Create image pull secret if using a private GHCR image:
```
``` bash
kubectl create secret docker-registry github-container-registry \
  --docker-server=ghcr.io \
  --docker-username=YOUR_GITHUB_USERNAME \
  --docker-password=YOUR_GITHUB_TOKEN \
  --docker-email=YOUR_EMAIL
```
## Apply manifests:

```bash
kubectl apply -f kubernetes/deployment.yaml
kubectl apply -f kubernetes/service.yaml
kubectl apply -f kubernetes/ingress.yaml
Pipeline and GitOps Architecture
Push or PR triggers GitHub Actions:
```
## Runs Maven tests, Checkstyle, SCA
```bash
Builds and pushes a Docker image (ghcr.io/bharathi-vbr/devsecops_demo:<sha>)
```
## Scans image security with Trivy

Updates deployment.yaml image field for GitOps

## ArgoCD Running in K8s:

Watches repo for manifest changes

## Syncs/deploys new versions in cluster automatically

## Monitoring & Scaling
See logs:

```bash
kubectl logs -l app=calculator
```
Scale up:

```bash
kubectl scale deployment calculator --replicas=5
```
Check status:

```bash
kubectl get all -n <your-namespace>
```
