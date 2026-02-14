import axios from 'axios';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3002';
const API_PREFIX = '/v1/api';

export interface Portfolio {
  id: string;
  userId: string;
  abbrevName: string;
  fullName: string;
  strategy?: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface Holding {
  id: string;
  portfolioId: string;
  isin: string;
  amount: number;
  price: number;
  holdDate: Date;
  createdAt: Date;
  updatedAt: Date;
}

export interface MarketBond {
  id: string;
  userId: string;
  isin: string;
  price: number;
  exchange?: string;
  date: Date;
  createdAt: Date;
  updatedAt: Date;
}

class PortfolioService {
  private getAuthHeader() {
    const token = localStorage.getItem('token');
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  }

  // Portfolio operations
  async createPortfolio(data: { abbrevName: string; fullName: string; strategy?: string }) {
    const response = await axios.post(`${API_BASE_URL}${API_PREFIX}/portfolios`, data, this.getAuthHeader());
    return response.data;
  }

  async getPortfolios(): Promise<Portfolio[]> {
    const response = await axios.get(`${API_BASE_URL}${API_PREFIX}/portfolios`, this.getAuthHeader());
    return response.data;
  }

  async getPortfolioById(portfolioId: string): Promise<Portfolio> {
    const response = await axios.get(`${API_BASE_URL}${API_PREFIX}/portfolios/${portfolioId}`, this.getAuthHeader());
    return response.data;
  }

  async updatePortfolio(portfolioId: string, data: Partial<Portfolio>) {
    const response = await axios.put(`${API_BASE_URL}${API_PREFIX}/portfolios/${portfolioId}`, data, this.getAuthHeader());
    return response.data;
  }

  async deletePortfolio(portfolioId: string) {
    const response = await axios.delete(`${API_BASE_URL}${API_PREFIX}/portfolios/${portfolioId}`, this.getAuthHeader());
    return response.data;
  }

  // Holdings operations
  async addHolding(portfolioId: string, data: { isin: string; amount: number; price: number; holdDate: string }) {
    const response = await axios.post(`${API_BASE_URL}${API_PREFIX}/portfolios/${portfolioId}/holdings`, data, this.getAuthHeader());
    return response.data;
  }

  async bulkAddHoldings(portfolioId: string, holdings: Array<{ isin: string; amount: number; price: number; holdDate: string }>) {
    const response = await axios.post(`${API_BASE_URL}${API_PREFIX}/portfolios/${portfolioId}/holdings/bulk`, { holdings }, this.getAuthHeader());
    return response.data;
  }

  async getHoldings(portfolioId: string): Promise<Holding[]> {
    const response = await axios.get(`${API_BASE_URL}${API_PREFIX}/portfolios/${portfolioId}/holdings`, this.getAuthHeader());
    return response.data;
  }

  async updateHolding(portfolioId: string, holdingId: string, data: Partial<Holding>) {
    const response = await axios.put(`${API_BASE_URL}${API_PREFIX}/portfolios/${portfolioId}/holdings/${holdingId}`, data, this.getAuthHeader());
    return response.data;
  }

  async deleteHolding(portfolioId: string, holdingId: string) {
    const response = await axios.delete(`${API_BASE_URL}${API_PREFIX}/portfolios/${portfolioId}/holdings/${holdingId}`, this.getAuthHeader());
    return response.data;
  }

  // Market bonds operations
  async createMarketBond(data: { isin: string; price: number; exchange?: string; date: string }) {
    const response = await axios.post(`${API_BASE_URL}${API_PREFIX}/market-bonds`, data, this.getAuthHeader());
    return response.data;
  }

  async bulkCreateMarketBonds(bonds: Array<{ isin: string; price: number; exchange?: string; date: string }>) {
    const response = await axios.post(`${API_BASE_URL}${API_PREFIX}/market-bonds/bulk`, { bonds }, this.getAuthHeader());
    return response.data;
  }

  async getMarketBonds(): Promise<MarketBond[]> {
    const response = await axios.get(`${API_BASE_URL}${API_PREFIX}/market-bonds`, this.getAuthHeader());
    return response.data;
  }

  async updateMarketBond(bondId: string, data: Partial<MarketBond>) {
    const response = await axios.put(`${API_BASE_URL}${API_PREFIX}/market-bonds/${bondId}`, data, this.getAuthHeader());
    return response.data;
  }

  async deleteMarketBond(bondId: string) {
    const response = await axios.delete(`${API_BASE_URL}${API_PREFIX}/market-bonds/${bondId}`, this.getAuthHeader());
    return response.data;
  }

  async deleteAllMarketBonds() {
    const response = await axios.delete(`${API_BASE_URL}${API_PREFIX}/market-bonds`, this.getAuthHeader());
    return response.data;
  }
}

const portfolioService = new PortfolioService();
export default portfolioService;
